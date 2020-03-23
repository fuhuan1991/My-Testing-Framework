public class AssertionObjObj {

  private final Object value;// the value can be null

  public AssertionObjObj(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return this.value;
  }

  public AssertionObjObj isNotNull() {
    if (this.value != null) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionObjObj isNull() {
    if (this.value == null) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionObjObj isEqualTo(Object o2) {
    if ((this.value == null && o2 == null) || this.value.equals(o2)) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionObjObj isNotEqualTo(Object o2) {
    if ((this.value == null && o2 != null) || !this.value.equals(o2)) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionObjObj isInstanceOf(Class c) {
    if (c.isInstance(this.value)) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

}
