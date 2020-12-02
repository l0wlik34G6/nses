package at.lowlik.nses;

import com.github.javafaker.Faker;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class WorkBufferLoad {

  private final MongoDatabase workBufferDatabase = DbHelper.getInstance().getDatabase("workBufferDb");

  private final Faker faker = new Faker();

  private final long numberOfElements = faker.number().numberBetween(4, 8);
  private final int startId = 8000;
  private final int interval = 50;

  public void loadDummyWorkBuffer() {
    MongoCollection<Document> entities = workBufferDatabase.getCollection("entity");
    entities.deleteMany(new BasicDBObject());
//    try {
    for (int i = 0; i < numberOfElements; i++) {
      entities.insertOne(
          new Document("entityId", faker.number().numberBetween(startId, startId + interval))
              .append("data",
                  new Document("firstname", faker.name().firstName())
                      .append("lastname", faker.name().lastName())
                      .append("birth", faker.date().birthday())
                      .append("country", faker.country().countryCode2())));
    }

//      entities.insertOne(new Document("entityId", faker.number().randomNumber(5, false)).append("data", new Document("firstname", faker.name().firstName()).append("lastname", "Yost")
//          .append("birth", sdf.parse("1992-06-29T20:27:08.200Z")).append("country", "RU")));
//      entities.insertOne(new Document("entityId", faker.number().randomNumber(5, false)).append("data", new Document("firstname", faker.name().firstName()).append("lastname", "Runolfsdottir")
//          .append("birth", sdf.parse("1986-10-04T07:03:11.069Z")).append("country", "FI")));
//      entities.insertOne(new Document("entityId", faker.number().randomNumber(5, false)).append("data", new Document("firstname", faker.name().firstName()).append("lastname", "Mosciski")
//          .append("birth", sdf.parse("1981-11-18T22:00:21.928Z")).append("country", "CZ")));
//      entities.insertOne(new Document("entityId", faker.number().randomNumber(5, false)).append("data", new Document("firstname", faker.name().firstName()).append("lastname", "White")
//          .append("birth", sdf.parse("1994-12-18T00:32:25.961Z")).append("country", "US")));
//      entities.insertOne(new Document("entityId", faker.number().randomNumber(5, false)).append("data", new Document("firstname", faker.name().firstName()).append("lastname", "McCullough")
//          .append("birth", sdf.parse("1995-11-14T18:02:50.543Z")).append("country", "UK")));
//    } catch (ParseException e) {
//      e.printStackTrace();
//    }
  }

}
