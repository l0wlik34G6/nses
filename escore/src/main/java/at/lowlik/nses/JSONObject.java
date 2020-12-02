package at.lowlik.nses;

import java.util.Date;
import java.util.HashMap;

public class JSONObject extends HashMap<String, Object> {

  public JSONObject(String jsonString) {
    super();
  }

  public Date getDate(String key) {
    return null;
  }

  public JSONObject put(String key, Date value) {
    return this;
  }

}
