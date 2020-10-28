package at.lowlik.nses.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class EntityData {

  private String firstname;
  private String lastname;
  private Date birth;
  private String country;

}
