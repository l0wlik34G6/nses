package at.lowlik.nses.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Event {

  private ObjectId relatedId;

  private EventObject data;

  private long version;

}
