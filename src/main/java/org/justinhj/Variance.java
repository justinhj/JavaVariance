package org.justinhj;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.money.format.MoneyAmountStyle;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.justinhj.domain.AmortizedLoan;
import org.justinhj.domain.CreditCard;
import org.justinhj.domain.Loan;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Variance {

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

    public static class CompareLoanPrincipal implements Comparator<Loan> {
        @Override
        public int compare(Loan o1, Loan o2) {
            return o1.getPrincipal().compareTo(o2.getPrincipal());
        }
    }

    // Find the largest loan in an array of loans
    public static Loan findLargest(Loan [] loans) {
        assert(loans.length > 0);
        return Arrays.stream(loans).max(new CompareLoanPrincipal()).get();
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


        // Loan amount calculator
        Money principal = Money.of(CurrencyUnit.CAD, 500000);

        MoneyFormatterBuilder mf = new MoneyFormatterBuilder();
        mf.appendCurrencySymbolLocalized();
        mf.appendAmount(MoneyAmountStyle.ASCII_DECIMAL_POINT_GROUP3_COMMA);
        MoneyFormatter mff = mf.toFormatter();

        System.out.println(mff.print(principal));

        // APR for a credit card with 18% interest rate and $200 yearly fee
        float apr = CreditCard.calculateAPR(Money.of(CurrencyUnit.CAD, 200), 18.0f);
        System.out.println(apr);

        CreditCard cc1 = new CreditCard(1234L, 1L, 18.0f);
        CreditCard cc2 = new CreditCard(1235L, 2L, 21.0f);
        AmortizedLoan al1 = new AmortizedLoan(1235L, 1L, 6.0f,
                Money.of(CurrencyUnit.CAD, 10000), 24, LocalDate.now());
        AmortizedLoan al2 = new AmortizedLoan(1235L, 1L, 9.0f,
                Money.of(CurrencyUnit.CAD, 25000), 60, LocalDate.now());

        Loan[] loans = {cc1,cc2,al1,al2};
        System.out.println("largest loan " + findLargest(loans));

        // Array variance
        CreditCard[] creditCards = {cc1,cc2};
        Loan[] creditCardLoans = creditCards;

        // BOOM ArrayStoreException a runtime error because arrays are covariant
        //creditCardLoans[1] = al1;

        // Can we find the largest of a credit card array?
        // Yes because covariant
        System.out.println("largest loan " + findLargest(creditCards));

        // Question, can you pass a super class?



    }

}
