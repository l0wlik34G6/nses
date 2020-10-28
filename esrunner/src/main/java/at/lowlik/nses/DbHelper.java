package at.lowlik.nses;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class DbHelper {

  private static final DbHelper _INSTANCE = new DbHelper();

  public static DbHelper getInstance() {
    return _INSTANCE;
  }

  private final MongoClient mongo;
  
  private DbHelper() {
    CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    mongo = new MongoClient("localhost", MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
  }

  public void close() {
    mongo.close();
  }

  public MongoDatabase getDatabase(String databaseName) {
    return mongo.getDatabase(databaseName);
  }
}
