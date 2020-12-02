package at.lowlik.nses.model.wb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EntityWorkBuffer {

  private int entityId;
  private EntityData data;
  private long version;

}
