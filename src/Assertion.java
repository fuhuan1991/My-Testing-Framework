public class Assertion {
    /* You'll need to change the return type of the assertThat methods */
    static AssertionObjObj assertThat(Object o) {
      return new AssertionObjObj(o);
    }
    static AssertionStrObj assertThat(String s) {
      return new AssertionStrObj(s);
    }
    static AssertionBooObj assertThat(boolean b) {
      return new AssertionBooObj(b);
    }
    static AssertionIntObj assertThat(int i) {
      return new AssertionIntObj(i);
    }
}