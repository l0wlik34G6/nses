package at.lowlik.nses.model;

import at.lowlik.nses.JSONObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class EventObject {

  @EqualsAndHashCode.Include
  private ObjectId id;

  private Document data;

  private String content;

  private int version;

  private Date changeDate;

  private char changeType;

}
