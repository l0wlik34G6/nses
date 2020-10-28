package at.lowlik.nses.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ModuleA extends Document {

  private ObjectId id;

  @BsonProperty(value = "firstname")
  private String firstName;

  @BsonProperty(value = "lastname")
  private String lastName;

  private Date birth;

  @BsonProperty(value = "content")
  private String content;

  private int version;

  private char changeType;

}

