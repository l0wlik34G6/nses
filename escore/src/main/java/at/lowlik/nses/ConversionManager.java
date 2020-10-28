package at.lowlik.nses;

import com.github.javafaker.Faker;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.java.Log;

@Log
public abstract class ConversionManager<T> extends ESServiceProvider implements ConversionIdentification {

  private MongoDatabase database;
  private MongoDatabase workBufferDatabase;
  private MongoCollection<T> collection;
  private int version;

  protected Faker faker = new Faker();

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

  public void setCollection(MongoCollection<T> collection) {
    this.collection = collection;
  }

  public MongoCollection<T> getCollection() {
    return collection;
  }

  protected final int getCurrentVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public void setDatabase(MongoDatabase database) {
    this.database = database;
  }

  public MongoDatabase getDatabase() {
    return database;
  }

  public void setWorkBufferDatabase(MongoDatabase workBufferDatabase) {
    this.workBufferDatabase = workBufferDatabase;
  }

  public MongoDatabase getWorkBufferDatabase() {
    return workBufferDatabase;
  }

}
