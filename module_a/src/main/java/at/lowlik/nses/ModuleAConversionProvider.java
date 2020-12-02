package at.lowlik.nses;

import at.lowlik.nses.model.EventObject;

public class ModuleAConversionProvider implements ConversionProvider {

  @Override
  public ConversionManager<? extends EventObject> create() {
    return new ModuleAConversionManager();
  }

}
