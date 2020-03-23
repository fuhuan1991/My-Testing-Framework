public class AssertionStrObj {

  private final String value;

  public AssertionStrObj(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public AssertionStrObj isNotNull() {
    if (this.value != null) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionStrObj isNull() {
    if (this.value == null) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionStrObj isEqualTo(Object s2) {
    if ((this.value == null && s2 == null) || this.value.equals(s2)) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionStrObj isNotEqualTo(Object s2) {
    if ((this.value == null && s2 != null) || !this.value.equals(s2)) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionStrObj startsWith(String b2) {
    if (this.value.startsWith(b2)) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionStrObj isEmpty() {
    if (this.value.isEmpty()) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public AssertionStrObj contains(String s2) {
    if (this.value.contains(s2)) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }
}
