package org.justinhj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Temp {

    public static Integer sumInteger(List<Integer> ints) {
        Integer s = 0;
        for (Integer n : ints) { s += n; }
        return s;
    }

    static Double sumNumbers(List<Number> nums) {
        Double n = 0.0;
        for(Number num: nums) {
            n = n + num.doubleValue();
        }
        return n;
    }

    public static int sum (List<Integer> ints) {
        int s = 0;
        for (int n : ints) { s += n; }
        return s;
    }

    public static void main(String[] args) {
        List<Integer> bigs = Arrays.asList(100,200,300);
        boolean s1 = sumInteger(bigs) == sum(bigs);
        // Warning number are compared with ==
        boolean s2 = sumInteger(bigs) != sumInteger(bigs); // not recommended

        System.out.println("s1 " + s1);
        System.out.println("s2 " + s2);

        // Boxing - Integers are compared by reference
        // ints are compared by value
        int a = 200;
        int b = 200;

        Integer a2 = 200;
        Integer b2 = 200;

        Integer a3 = 100;
        Integer b3 = 100;

        boolean bool1 = Integer.valueOf(20) == Integer.valueOf(20);
        boolean bool2 = Integer.valueOf(200) == Integer.valueOf(200);

        System.out.println("a == b " + (a == b)); // true
        System.out.println("a2 == b2 " + (a2 == b2)); // false (objects are different)
        System.out.println("a3 == b3 " + (a3 == b3)); // true (small numbers are cached)
        System.out.println("a == a2 " + (a == a2));  // true

        //System.out.println("a equals b " + (a.equals(b))); // cannot compile
        System.out.println("a2 equals b2 " + (a2.equals(b2))); // false (objects are different)
        System.out.println("a3 equals b3 " + (a3.equals(b3))); // true (small numbers are cached)
        System.out.println("a equals a2 " + (a2.equals(a)));  // true

        // Making generic lists
        // the are made explicitly from arrays, for example
        // uses the varargs template arg `static <E> List<E> of(E... elements)`
        List<Integer> integerList = List.of(new Integer[]{1,2,3});
        List<Integer> integerList2 = List.of(1,2,3); // equivalent

        List<Serializable> serializables1 = List.of(1, "Hello", 3.0);

        // Polymorphic list adding
        List<Number> numbers = new ArrayList<>();
        numbers.add(1.0f);
        numbers.add(10L);
        numbers.add(42);
        System.out.println("Sum " + sumNumbers(numbers)); // Gross but it works

        List<Integer> ints = new ArrayList<Integer>();
        ints.add(1);
        ints.add(2);
        // Incompatible types. Found Integer required Number
//        List<Number> nums = ints; // compile-time error
//        nums.add(3.14);
//        assert ints.toString().equals("[1, 2, 3.14]"); // uh oh!

        // The above is not allowed because List<Integer> is not a subtype of List<Number>
        // Here is the same thing in reverse

        List<Number> nums = new ArrayList<Number>();
        nums.add(2.78);
        nums.add(3.14);
        // wrong type...
//        List<Integer> ints2 = nums; // compile-time error
//        assert ints2.toString().equals("[2.78, 3.14]"); // uh oh!

        // This shows how with a covariant, producer, extends we cannot add to the structure...

        List<Integer> ints98 = new ArrayList<Integer>();
        ints98.add(1);
        ints98.add(2);
        List<? extends Number> nums101 = ints98;
//        nums101.add(3.14); // compile-time error
//        assert ints98.toString().equals("[1, 2, 3.14]"); // uh oh!

        // COVARIANCE
        // Can read from the list (producer)
        List<Integer> myInts = List.of(1,2,3);

        List<? extends Number> myNums = myInts;

        // reading from myInts is ok
        System.out.println("myInts(0) = " + myNums.get(0));

        // writing is not - type error
        //myNums.add(10L);
    }
}
