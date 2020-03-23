import com.sun.jdi.event.StepEvent;

import java.util.*;

public class TestSuit {

  private static int counter;


//  @BeforeClass
//  public static void beforeClassB() {
//    System.out.println("beforeClassB launched");
//  }
//  @BeforeClass
//  public static void beforeClassA() {
//    System.out.println("beforeClassA launched");
//  }
//  @AfterClass
//  public static void afterClassB() {
//    System.out.println("afterClassB launched");
//  }
//  @AfterClass
//  public static void afterClassA() {
//    System.out.println("afterClassA launched");
//  }
//  @Before
//  public void beforeB() {
//    System.out.println("beforeB launched");
//  }
//  @Before
//  public void beforeA() {
//    System.out.println("beforeA launched");
//  }
//  @After
//  public void afterB() {
//    System.out.println("afterB launched");
//  }
//  @After
//  public void afterA() {
//    System.out.println("afterA launched");
//  }

//  @Test
//  public void mA() {
//    System.out.println("mA launched");
//    Assertion.assertThat(42).isEqualTo(42).isGreaterThan(12).isLessThan(522);
//  }
//
//  @Test
//  public void mB() {
//    System.out.println("mB launched");
//    Assertion.assertThat(true).isTrue().isEqualTo(true);
//  }
//
//  @Test
//  public void mC() {
//    System.out.println("mC launched");
//    Assertion.assertThat("abc").isNotNull().isEqualTo("abc").isNotEqualTo("bvc");
//    Assertion.assertThat("huan fu").startsWith("hu").contains("huan");
//    Assertion.assertThat("").isEmpty();
//  }
//
//  @Test
//  public void md() {
//    System.out.println("mD launched");
//    HashMap<Integer, Integer> d1 = new HashMap<>();
//    HashMap<Integer, Integer> d2 = new HashMap<>();
//    d1.put(1,1);
//    Assertion.assertThat(d1).isEqualTo(null);
//  }

//  @Property
//  public boolean pB(@IntRange(min=-2, max=2) Integer i,  @StringSet(strings={"s1", "s2", "s3"}) String name) {
//    System.out.println("pB:" + i + ", " + name);
//    if (i.equals(1) && name.equals("s2")) {
//      System.out.println("!!!!!!!!!!");
//      return false;
//    }
//    return true;
//  }
//
//  @Property
//  public boolean pA(@IntRange(min=-2, max=2) Integer i) {
//    return Math.abs(i.intValue()) >= 0;
//  }

  @Property
  public boolean pC(@ListLength(min=1, max=2) List<@ListLength(min=1, max=2) List<@IntRange(min=5, max=5) Integer>> list) {
    System.out.println(list);
    return true;
  }

//  @Property
//  public boolean pD(@ForAll(name="genIntSet", times=10) Object o) {
////    System.out.println();
//    Set s = (Set) o;
//    return true;
//  }

  int count = 0;

  public Object genIntSet() {
    Set s = new HashSet();
    for (int i=0; i<count; i++) { s.add(i); }
    count++;
    return s;
  }
}
