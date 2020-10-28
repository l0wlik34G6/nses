package at.lowlik.nses;

public abstract class ESServiceProvider implements Comparable<ESServiceProvider> {

  private static final int PRIORITY_HIGH = 100;

  public boolean isEnabled() {
    return true;
  }

  /**
   * return priority of the service provider, where 100 is the highest priority and the lower the number the lower the priority
   */
  public int priority() {
    return PRIORITY_HIGH;
  }

  @Override
  public int compareTo(ESServiceProvider otherProvider) {
    if(otherProvider.priority() > this.priority()) {
      return 1;
    } else if(otherProvider.priority() < this.priority()) {
      return -1;
    }
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
