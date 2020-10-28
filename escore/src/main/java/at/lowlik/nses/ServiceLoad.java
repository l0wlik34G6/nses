package at.lowlik.nses;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ServiceLoad {

  private static final ServiceLoader<ConversionProvider> loader;

  static {
    loader = ServiceLoader.load(ConversionProvider.class);
  }

  public static Iterator<ConversionProvider> providers(boolean refresh) {
    if (refresh) {
      loader.reload();
    }
    return loader.iterator();
  }

}
