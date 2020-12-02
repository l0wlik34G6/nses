package at.lowlik.nses;

import at.lowlik.nses.model.Event;
import at.lowlik.nses.model.wb.EntityWorkBuffer;
import at.lowlik.nses.model.Entity;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import lombok.extern.java.Log;
import org.bson.Document;

@Log
public class ModuleAConversionManager extends ConversionManager<Entity> {

  @Override
  public Class<Entity> getRepositoryClass() {
    return Entity.class;
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

    MongoCollection<Entity> entities = getDatabase().getCollection("entity", Entity.class);

    MongoCollection<EntityWorkBuffer> entitiesInWorkBuffer = getWorkBufferDatabase().getCollection("entity", EntityWorkBuffer.class);
    log.info("Workbuffer count: " + entitiesInWorkBuffer.count());

//    entities.find().forEach(new Consumer<Entity>() {
//      @Override
//      public void accept(Entity entity) {
//
//      }
//    });

    for(EntityWorkBuffer entityWorkBuffer : entitiesInWorkBuffer.find().sort(Sorts.ascending("entityId"))) {

      Entity entity = entities.find(new BasicDBObject("entityId", entityWorkBuffer.getEntityId())).first();
      if(entity != null) {
        if(entityWorkBuffer.getData().getBirth().compareTo(entity.getData().getDate("birth")) != 0) {
          entity.getData().put("birth", entityWorkBuffer.getData().getBirth());

          updateEventObject(entity, entities, "entityEvents");
        }
      } else {
        entity = new Entity();
        entity.setEntityId(entityWorkBuffer.getEntityId());
        entity.setData(new Document());
        entity.getData().put("firstname", entityWorkBuffer.getData().getFirstname());
        entity.getData().put("lastname", entityWorkBuffer.getData().getLastname());
        entity.getData().put("birth", entityWorkBuffer.getData().getBirth());

        insertEventObject(entity, entities);
      }
    }

  }

  @Override
  public void cleanUp() {
    log.info("Module A: cleanUp");

    getWorkBufferDatabase().getCollection("entity").deleteMany(new BasicDBObject());
  }

  @Override
  public boolean hasChanges() {
    return true;
  }

}
