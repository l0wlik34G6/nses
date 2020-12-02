package at.lowlik.nses;

public class ESRunner {

  public static void main(String[] args) {

    try {

      new WorkBufferLoad().loadDummyWorkBuffer();

      new Conversion().run("TYPE-A", "EN");
//      new Conversion().run("TYPE-B", "EN");

    } finally {
      DbHelper.getInstance().close();
    }
  }

}
