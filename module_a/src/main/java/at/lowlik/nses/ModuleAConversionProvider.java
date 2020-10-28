package at.lowlik.nses;

public class ModuleAConversionProvider implements ConversionProvider {

  @Override
  public ConversionManager create() {
    return new ModuleAConversionManager();
  }

}
