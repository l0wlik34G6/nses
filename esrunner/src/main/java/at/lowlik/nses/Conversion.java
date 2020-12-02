package at.lowlik.nses;

import at.lowlik.nses.model.Content;
import at.lowlik.nses.model.EventObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.java.Log;
import org.bson.json.JsonWriterSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Log
public class Conversion {

  private final MongoDatabase coreDatabase = DbHelper.getInstance().getDatabase("coreDb");

  Content content;

  public void run(String type, String language) {

    MongoCollection<Content> contentCollection = coreDatabase.getCollection("content", Content.class);

    List<ConversionManager<? extends EventObject>> managerList = new ArrayList<>();

    ServiceLoad.providers(true).forEachRemaining(conversionProvider -> {
      ConversionManager<? extends EventObject> manager = conversionProvider.create();
      if (manager.isEnabled() &&
          (manager.getType() == null || type.equals(manager.getType())) &&
          (manager.getLanguage() == null || language.equals(manager.getLanguage()))) {
        managerList.add(manager);
      }
    });

    Collections.sort(managerList);

    for (ConversionManager<? extends EventObject> manager : managerList) {
      content = contentCollection.find(eq("contentId", manager.getRepositoryClass().getSimpleName())).first();
      if (content == null) {
        content = new Content();
        content.setContentId(manager.getRepositoryClass().getSimpleName());
        content.setVersion(0);
        contentCollection.insertOne(content);
      }
      log.info("Content: " + content.toString());
      manager.setWorkBufferDatabase(DbHelper.getInstance().getDatabase("workBufferDb"));
      manager.setDatabase(DbHelper.getInstance().getDatabase(manager.getRepositoryClass().getSimpleName()));

      if (manager.hasChanges()) {
        manager.setVersion(content.getVersion() + 1);
        try {
          manager.preConversion();
          manager.conversion();
          manager.postConversion();
          manager.cleanUp();
          incrementContentVersion(manager, contentCollection);
        } catch(Exception ex) {
          log.severe(ex.getMessage());
          ex.printStackTrace();
        }
      } else {
        manager.noChangesAction();
      }
    }

  }

  private void incrementContentVersion(ConversionManager<? extends EventObject> manager, MongoCollection<Content> contentCollection) {
    content.setVersion(content.getVersion() + 1);
    contentCollection.replaceOne(eq("contentId", manager.getRepositoryClass().getSimpleName()), content);
  }

}
