package at.lowlik.nses;

import at.lowlik.nses.model.Entity;
import at.lowlik.nses.model.ModuleA;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;
import lombok.extern.java.Log;
import org.bson.json.JsonWriterSettings;

@Log
public class ModuleAConversionManager extends ConversionManager<ModuleA> {

  @Override
  public Class<ModuleA> getRepositoryClass() {
    return ModuleA.class;
  }

  @Override
  public String getType() {
    return "TYPE-A";
  }

  @Override
  public void preConversion() {
    log.info("Module A: Pre-Conversion");
  }

  @Override
  public void conversion() {
    log.info("Module A: Conversion");

    setCollection(getDatabase().getCollection("names", ModuleA.class));

    FindIterable<ModuleA> iterableDocument = getCollection().find();
    MongoCursor<ModuleA> cursor = iterableDocument.iterator();
    cursor.forEachRemaining(moduleA -> log.info(moduleA.toJson(JsonWriterSettings.builder().build())));

    MongoCollection<Entity> entities = getWorkBufferDatabase().getCollection("entity", Entity.class);

    log.info("Workbuffer count: " + entities.count());
    for(Entity entity : entities.find().sort(Sorts.ascending("entityId"))) {

      boolean insertOrUpdate = false;

      ModuleA moduleA = getCollection().find(new BasicDBObject("firstname", entity.getData().getFirstname()).append("lastname", entity.getData().getLastname())).first();
      if(moduleA != null) {
        if(entity.getData().getBirth().compareTo(moduleA.getBirth()) != 0) {
          moduleA.setBirth(entity.getData().getBirth());
          moduleA.setVersion(getCurrentVersion());
          moduleA.setChangeType('U');
        }
      } else {
        moduleA = new ModuleA();
        moduleA.setContent("moduleA");
        moduleA.setFirstName(entity.getData().getFirstname());
        moduleA.setLastName(entity.getData().getLastname());
        moduleA.setBirth(entity.getData().getBirth());
        moduleA.setVersion(getCurrentVersion());
        moduleA.setChangeType('I');

        insertOrUpdate = true;
      }

      if(insertOrUpdate) {
        log.info("Insert or update ...");
        this.getCollection().insertOne(moduleA);
      }
    }

  }

  @Override
  public void cleanUp() {
    log.info("Module A: cleanUp");

    getWorkBufferDatabase().getCollection("entity").drop();
  }

  @Override
  public boolean hasChanges() {
    return true;
  }

}
