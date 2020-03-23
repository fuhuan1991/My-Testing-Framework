import javax.naming.OperationNotSupportedException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class Unit {
    public static HashMap<String, Throwable> testClass(String name) throws Exception{

      Class<?> targetClass = Class.forName(name);
      Method[] mList = targetClass.getMethods();
      Object testObj = targetClass.getConstructor().newInstance();
      ArrayList<Method> testList = new ArrayList<>();
      ArrayList<Method> afterList = new ArrayList<>();
      ArrayList<Method> afterClassList = new ArrayList<>();
      ArrayList<Method> beforeList = new ArrayList<>();
      ArrayList<Method> beforeClassList = new ArrayList<>();
      HashMap<String, Throwable> result = new HashMap<>();

      // analyze every method
      for (Method m : mList) {
        Annotation[] aList = m.getDeclaredAnnotations();

        boolean specialAnnotationFound = false;
        //analyze all the annotations on a single method
        for(Annotation a : aList) {

          // a is a special annotation
          if (a instanceof  Test || a instanceof Before || a instanceof BeforeClass || a instanceof After || a instanceof AfterClass) {
            if (specialAnnotationFound) {
              throw new UnsupportedOperationException();
            } else {
              specialAnnotationFound = true;
            }
          }

          if (a instanceof Test) {
            testList.add(m);
          }

          if (a instanceof BeforeClass) {
            if (!Modifier.isStatic(m.getModifiers())) throw new UnsupportedOperationException();
            beforeClassList.add(m);
          }

          if (a instanceof Before) {
            beforeList.add(m);
          }

          if (a instanceof After) {
            afterList.add(m);
          }

          if (a instanceof AfterClass) {
            if (!Modifier.isStatic(m.getModifiers())) throw new UnsupportedOperationException();
            afterClassList.add(m);
          }
        }
      }

      // sort all the test cases according alphabetical order
      testList.sort(Comparator.comparing(Method::getName));
      beforeClassList.sort(Comparator.comparing(Method::getName));
      beforeList.sort(Comparator.comparing(Method::getName));
      afterList.sort(Comparator.comparing(Method::getName));
      afterClassList.sort(Comparator.comparing(Method::getName));

      //execute all the test cases/////////////////////////////////
      executeList(beforeClassList, testObj); // execute @BeforeClass methods

      for (Method kase : testList) {
        executeList(beforeList, testObj); // execute @Before methods
        try {
          kase.invoke(testObj); // execute @Test methods
          result.put(kase.getName(), null);
        } catch (Exception e) {
          result.put(kase.getName(), e.getCause());
        }
        executeList(afterList, testObj); // execute @After methods
      }
      executeList(afterClassList, testObj); // execute @AfterClass methods
      ////////////////////////////////////////////////////////////////
      System.out.println(result);
      return result;
    }

    private static void executeList (ArrayList<Method> list, Object testObj) throws Exception {
      for (Method m : list) {
        m.invoke(testObj);
      }
    }

    public static HashMap<String, Object[]> quickCheckClass(String name) throws Exception {

      Class<?> targetClass = Class.forName(name);
      Method[] mList = targetClass.getMethods();
      Object testObj = targetClass.getConstructor().newInstance();
      ArrayList<Method> pList = new ArrayList<>();
      HashMap<String, Object[]> finalResult = new HashMap<>();

      // get all the properties
      for (Method m : mList) {
        Annotation[] aList = m.getDeclaredAnnotations();
        for (Annotation a : aList) {
          if (a instanceof Property) pList.add(m);
        }
      }

      // sort properties according to alphabetical order
      pList.sort(Comparator.comparing(Method::getName));

      // loop through all properties
      for (Method property : pList) {

        /*
        * prepare the combination of possible parameters for every property
        * */
        // check all parameters
        Parameter[] pareList = property.getParameters();
        // this is the list of parameter list
        ArrayList<ArrayList<Object>> inputArray = new ArrayList<>();
        // generate all the possible value for a parameter
        for (Parameter p : pareList) {
          Annotation anno = p.getAnnotations()[0];
          if (anno instanceof IntRange) {
            inputArray.add(getIntInputArray((IntRange) anno));
          }
          if (anno instanceof StringSet) {
            inputArray.add(getStrInputArray((StringSet) anno));
          }
          if (anno instanceof ListLength) {
            inputArray.add(getLisInputArray((AnnotatedParameterizedType) p.getAnnotatedType(), targetClass));
          }
          if (anno instanceof ForAll) {
            inputArray.add(getObjInputArray((ForAll) anno, targetClass));
          }
        }
        ArrayList<Object> parameters = generateAllCombinations(inputArray);

        /*
        * Execute the property method
        * */
        boolean error = false;
        int counter = 0;

        for (Object paraList : parameters) {
          if (counter >= 100) break;
          Object[] arr = ((List) paraList).toArray();
          Object r;
          try{
            r = property.invoke(testObj, arr);
            counter++;
          } catch (Exception e) {
            finalResult.put(property.getName(), arr);
            error = true;
            break;
          }
          if(!(Boolean) r) {
            // if the result of any property method is false, stop executing others and record the parameters.
            finalResult.put(property.getName(), arr);
            error = true;
            break;
          }
        }

        // everything is OK
        if (!error) finalResult.put(property.getName(), null);
      }

      return finalResult;
    }

    private static ArrayList<Object> getIntInputArray(IntRange anno) {

      int current = anno.min();
      int max = anno.max();
      ArrayList<Object> input = new ArrayList<>();

      while (current <= max) {
        input.add(current);
        current++;
      }
      return input;
    }

    private static ArrayList<Object> getStrInputArray(StringSet anno) {
      return new ArrayList<Object>(Arrays.asList(anno.strings()));
    }

    private static ArrayList<Object> getLisInputArray(AnnotatedParameterizedType p, Class targetClass) {
//      System.out.println("p: " + p);

      ArrayList<Object> input = new ArrayList<>();
      int minLen = p.getAnnotation(ListLength.class).min();
      int maxLen = p.getAnnotation(ListLength.class).max();
//      AnnotatedParameterizedType T = (AnnotatedParameterizedType) p.getAnnotatedType();
      Annotation annoT = p.getAnnotatedActualTypeArguments()[0].getAnnotations()[0];
      ArrayList<Object> possibleValues = new ArrayList<>();

      if (annoT instanceof IntRange) possibleValues = getIntInputArray((IntRange) annoT);
      if (annoT instanceof StringSet) possibleValues = getStrInputArray((StringSet) annoT);
      if (annoT instanceof ForAll) possibleValues = getObjInputArray((ForAll) annoT, targetClass);
      if (annoT instanceof ListLength) possibleValues = getLisInputArray((AnnotatedParameterizedType)p.getAnnotatedActualTypeArguments()[0], targetClass);

      // generate the list of different length
      for (int len = minLen; len <= maxLen; len++) {
        if (len == 0) {
          if (annoT instanceof IntRange) input.add(new ArrayList<Integer>());
          if (annoT instanceof StringSet) input.add(new ArrayList<String>());
          if (annoT instanceof ForAll || annoT instanceof ListLength) input.add(new ArrayList<Object>());
        } else {
          ArrayList<ArrayList<Object>> radixArray = new ArrayList<>();
          for (int i = 1; i <= len; i++) {
            radixArray.add(possibleValues);
          }

          input.addAll(generateAllCombinations(radixArray));
//          System.out.println(generateAllCombinations(radixArray));
        }
      }

      return input;
    }

    // given a list of lists, return all the possible combinations.
    public static ArrayList<Object> generateAllCombinations(ArrayList<ArrayList<Object>> radixArray) {
      int len = radixArray.size();
      int[] radix = new int[len];

      for (int i = 0; i <= len-1; i++) {
        radix[i] = radixArray.get(i).size();
      }

//      System.out.println("radix: " + Arrays.toString(radix));
      IndexGenerator IG = new IndexGenerator(radix);
      ArrayList<Object> result = new ArrayList<>();

      while (IG.hasNext()) {
        int[] index = IG.next();

        ArrayList<Object> temp = new ArrayList<>();

        for (int d = 0; d <= index.length-1; d++) {
          temp.add(radixArray.get(d).get(index[d]));
        }
        result.add(temp);
      }
//      System.out.println(result);
      return result;
    }

    public static ArrayList<Object> getObjInputArray(ForAll anno, Class targetClass) {
      String name = anno.name();
      int times = anno.times();
      ArrayList<Object> input = new ArrayList<>();
      try {
        Method m = targetClass.getMethod(name);
        Object testObj = targetClass.getConstructor().newInstance();
        for (int i = 1; i <= times; i++) {
          Object r = m.invoke(testObj);
          input.add(r);
        }
      } catch (Exception e) {
        System.out.println(e);
      }
      return input;
    }

    public static void main(String[] args) {
      try {
//        testClass("TestSuit");
        HashMap<String, Object[]> r = quickCheckClass("TestSuit");
        System.out.println(r);
//        System.out.println(Arrays.toString(r.get("pB")));
      } catch (Exception e){
        System.out.println(e);
      }

    }
}