package at.lowlik.nses;

import at.lowlik.nses.model.Event;
import at.lowlik.nses.model.EventObject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import lombok.extern.java.Log;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.conversions.Bson;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log
public abstract class ConversionManager<T extends EventObject> extends ESServiceProvider implements ConversionIdentification {

  private MongoDatabase database;
  private MongoDatabase workBufferDatabase;
  private int version;
  private Map<String, String> keyFields;

  public void preConversion() {
    log.info("Manager: preConversion");
  }

  public abstract void conversion();

  public void postConversion() {
    log.info("Manager: postConversion");
  }

  public void noChangesAction() {
    log.info("Manager: noChangesAction");
    cleanUp();
  }

  public abstract void cleanUp();

  public abstract boolean hasChanges();

  public abstract Class<T> getRepositoryClass();

  protected final int getCurrentVersion() {
    return version;
  }

  public final void setVersion(int version) {
    this.version = version;
  }

  public final void setDatabase(MongoDatabase database) {
    this.database = database;
  }

  public final MongoDatabase getDatabase() {
    return database;
  }

  public final void setWorkBufferDatabase(MongoDatabase workBufferDatabase) {
    this.workBufferDatabase = workBufferDatabase;
  }

  public final MongoDatabase getWorkBufferDatabase() {
    return workBufferDatabase;
  }

  protected final void updateEventObject(T eventObject, MongoCollection<T> objectCollection, String eventCollectionName) {
    eventObject.setVersion(getCurrentVersion());
    eventObject.setChangeDate(new Date());
    eventObject.setChangeType('U');

    fetchKeyFieldsFromEventObject(eventObject);

    //todo: fetch current object based on annotations of fields in T
    T currentObject = objectCollection.find(new BasicDBObject("_id", eventObject.getId())).first();

    //todo: copy data to a new event
    Event event = new Event();
    event.setRelatedId(currentObject.getId());
    event.setData(currentObject);
    event.setVersion(currentObject.getVersion());
    getDatabase().getCollection(eventCollectionName, Event.class).insertOne(event);

    //todo: filter out object based on annotations of fields in T
    objectCollection.updateOne(new BasicDBObject("_id", eventObject.getId()), Updates.currentDate("changeDate"));
    objectCollection.updateOne(new BasicDBObject("_id", eventObject.getId()), Updates.set("version", getCurrentVersion()));
    objectCollection.updateOne(new BasicDBObject("_id", eventObject.getId()), Updates.set("changeType", 'U'));
    objectCollection.updateOne(new BasicDBObject("_id", eventObject.getId()), Updates.set("data", eventObject.getData()));
  }

  protected final void insertEventObject(T eventObject, MongoCollection<T> collection) {
    eventObject.setVersion(getCurrentVersion());
    eventObject.setChangeDate(new Date());
    eventObject.setContent("moduleA");
    eventObject.setChangeType('I');

    fetchKeyFieldsFromEventObject(eventObject);

    collection.insertOne(eventObject);
  }

  private void fetchKeyFieldsFromEventObject(T eventObject) {
    if(keyFields == null) {
      keyFields = new HashMap<>();
      for (Field field : eventObject.getClass().getDeclaredFields()) {
        String dbField = field.getName();
        if(field.isAnnotationPresent(BsonProperty.class)) {
          dbField = field.getAnnotation(BsonProperty.class).value();
        }
        keyFields.put(dbField, field.getName());
      }
    }
  }

}
