public class AssertionIntObj {

  private final int value;

  public AssertionIntObj(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public AssertionIntObj isEqualTo(int i2) {
    if (this.value == i2) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionIntObj isLessThan(int i2) {
    if (this.value < i2) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionIntObj isGreaterThan(int i2) {
    if (this.value > i2) {
      return this;
    } else {
      throw new RuntimeException();
      //this.value + " isGreaterThan " + i2
    }
  }

}
