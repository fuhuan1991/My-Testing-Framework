public class AssertionBooObj {
  private final boolean value;

  public AssertionBooObj(boolean value) {
    this.value = value;
  }

  public boolean getValue() {
    return value;
  }

  public AssertionBooObj isEqualTo(boolean b2) {
    if (this.value == b2) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionBooObj isTrue() {
    if (this.value) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionBooObj isFalse() {
    if (!this.value) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }
}

