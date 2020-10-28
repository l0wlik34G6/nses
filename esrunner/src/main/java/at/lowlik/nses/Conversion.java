package at.lowlik.nses;

import at.lowlik.nses.model.Content;
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

    List<ConversionManager<? extends ESServiceProvider>> managerList = new ArrayList<>();

    ServiceLoad.providers(true).forEachRemaining(conversionProvider -> {
      ConversionManager<? extends ESServiceProvider> manager = conversionProvider.create();
      if (manager.isEnabled() &&
          (manager.getType() == null || type.equals(manager.getType())) &&
          (manager.getLanguage() == null || language.equals(manager.getLanguage()))) {
        managerList.add(manager);
      }
    });

    Collections.sort(managerList);

    for (ConversionManager<? extends ESServiceProvider> manager : managerList) {
      content = contentCollection.find(eq("contentId", manager.getRepositoryClass().getSimpleName())).first();
      if (content == null) {
        content = new Content();
        content.setContentId(manager.getRepositoryClass().getSimpleName());
        content.setVersion(0);
        contentCollection.insertOne(content);
      }
      log.info("Content: " + content.toJson(JsonWriterSettings.builder().build()));
      manager.setWorkBufferDatabase(DbHelper.getInstance().getDatabase("workBufferDb"));
      manager.setDatabase(DbHelper.getInstance().getDatabase(manager.getRepositoryClass().getSimpleName()));

      if (manager.hasChanges()) {
        incrementContentVersion(manager, contentCollection);
        manager.preConversion();
        manager.conversion();
        manager.postConversion();
        manager.cleanUp();
      } else {
        manager.noChangesAction();
      }
    }

  }

  private void incrementContentVersion(ConversionManager<? extends ESServiceProvider> manager, MongoCollection<Content> contentCollection) {
    content.append("version", 0);
    contentCollection.replaceOne(eq("contentId", manager.getRepositoryClass().getSimpleName()), content);

    manager.setVersion(content.getInteger("version"));
  }

}
