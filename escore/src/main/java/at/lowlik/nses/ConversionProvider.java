package at.lowlik.nses;

import at.lowlik.nses.model.EventObject;

public interface ConversionProvider {

  ConversionManager<? extends EventObject> create();

}
