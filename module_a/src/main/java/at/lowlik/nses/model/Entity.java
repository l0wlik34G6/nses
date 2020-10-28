package at.lowlik.nses.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Entity {

  private int entityId;
  private EntityData data;

}
