package at.lowlik.nses;

public interface ConversionIdentification {

  String getType();

  default String getLanguage() {
    return null;
  }

}