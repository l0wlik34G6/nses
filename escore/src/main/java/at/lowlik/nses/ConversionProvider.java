package at.lowlik.nses;

public interface ConversionProvider {

  ConversionManager<? extends ESServiceProvider> create();

}
