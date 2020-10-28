package at.lowlik.nses;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class WorkBufferLoad {

  private final MongoDatabase workBufferDatabase = DbHelper.getInstance().getDatabase("workBufferDb");

  public void loadDummyWorkBuffer() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
    MongoCollection<Document> entities = workBufferDatabase.getCollection("entity");
    try {
      entities.insertOne(new Document("entityId", 163456).append("data", new Document("firstname", "So").append("lastname", "Yost")
          .append("birth", sdf.parse("1993-06-29T20:27:08.200Z")).append("country", "RU")));
      entities.insertOne(new Document("entityId", 168724).append("data", new Document("firstname", "Ammie").append("lastname", "Runolfsdottir")
          .append("birth", sdf.parse("1986-10-04T07:03:11.069Z")).append("country", "FI")));
      entities.insertOne(new Document("entityId", 123894).append("data", new Document("firstname", "Lanie").append("lastname", "Mosciski")
          .append("birth", sdf.parse("1981-11-18T22:00:21.928Z")).append("country", "CZ")));
      entities.insertOne(new Document("entityId", 185354).append("data", new Document("firstname", "Long").append("lastname", "White")
          .append("birth", sdf.parse("1995-12-18T00:32:25.961Z")).append("country", "US")));
      entities.insertOne(new Document("entityId", 110357).append("data", new Document("firstname", "Jed").append("lastname", "McCullough")
          .append("birth", sdf.parse("1995-11-14T18:02:50.543Z")).append("country", "UK")));
      entities.insertOne(new Document("entityId", 76248).append("data", new Document("firstname", "Parker").append("lastname", "Koepp")
          .append("birth", sdf.parse("1956-10-22T22:46:33.745Z")).append("country", "DE")));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

}
