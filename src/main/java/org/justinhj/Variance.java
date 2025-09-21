package org.justinhj;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.money.format.MoneyAmountStyle;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.justinhj.domain.AmortizedLoan;
import org.justinhj.domain.CreditCard;
import org.justinhj.domain.FinancialInstrument;
import org.justinhj.domain.Loan;
import org.justinhj.util.LoanReportPrinter;
import org.justinhj.util.FinancialInstrumentReportPrinter;
import org.justinhj.util.ReportPrinter;
import org.justinhj.util.LoanComparators.CompareLoanOverdueDays;
import org.justinhj.util.LoanComparators.CompareLoanPrincipal;

import java.time.LocalDate;
import java.util.*;

// Variance examples using a pretend financial institution model
public class Variance {

    // Find the largest loan in an array of loans
    public static Loan findLargest(Loan [] loans) {
        assert(loans.length > 0);
        return Arrays.stream(loans).max(new CompareLoanPrincipal()).get();
    }

    public static Loan findLargestList(List<Loan> loans) {
        assert(loans.size() > 0);
        return loans.stream().max(new CompareLoanPrincipal()).get();
    }

    public static Loan findLargestListFixed(List<? extends Loan> loans) {
        assert(loans.size() > 0);
        return loans.stream().max(new CompareLoanPrincipal()).get();
    }

    public static void getClientLoans(Long clientId, List<? extends Loan> loans, List<? super Loan> output) {
        loans.stream().filter(loan -> loan.getClientId().equals(clientId)).forEach(
                loan -> output.add(loan)
        );

    }

    // Wilcard capture (§2.7 Java Generics and Collections)

    // With this type signature which is simpler than the next one we are saying
    // reverse a list with unknown type (?). The implementation in reverse2 below
    // does not allow us to
    //    public static void reverseBroken(List<?> list) {
    //        for(int i=0, j=list.size() - 1;i<j;i++,j--) {
    //            Object ej = list.get(j);
    //            list.set(j,list.get(i)); // "required capture of ?" or similar error, the compiler doesn't know the type
    //            list.set(i,ej); // same error
    //        }
    //    }

    public static void reverse1(List<?> list) {
        reverse2(list); // instead call the other function (Which would be private) and type inference "captures"
        // the wildcard
    }

    public static <E> void reverse2(List<E> list) {
        for(int i=0, j=list.size() - 1;i<j;i++,j--) {
            E ej = list.get(j);
            list.set(j,list.get(i));
            list.set(i,ej);
        }
    }

    // Also https://docs.oracle.com/javase/tutorial/java/generics/capture.html

    enum DieRolls {
        One,
        Two,
        Three
    }

    // Function for contravariance example, a print reports for credit cards
    // But we may not have want a report printer specifically for them so
    // specify super and that allows contravariance of the report printer
    public static void creditCardReports(Collection<? extends CreditCard> reports,
                                         ReportPrinter<? super CreditCard> reportPrinter) {
        for (CreditCard report : reports) {
            reportPrinter.report(report);
        }
    }
    @SuppressWarnings("unused")
    public static void main(String[] args) {

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

        CreditCard cc1 = new CreditCard(1234L, 1L, 18.0f, Money.of(CurrencyUnit.CAD, 500));
        CreditCard cc2 = new CreditCard(1235L, 2L, 21.0f, Money.of(CurrencyUnit.CAD, 200));
        AmortizedLoan al1 = new AmortizedLoan(1235L, 1L, 6.0f,
                Money.of(CurrencyUnit.CAD, 10000), 24, LocalDate.now().minusDays(10));
        AmortizedLoan al2 = new AmortizedLoan(1235L, 1L, 9.0f,
                Money.of(CurrencyUnit.CAD, 25000), 60, LocalDate.now());
        AmortizedLoan al3 = new AmortizedLoan(1235L, 1L, 9.0f,
                Money.of(CurrencyUnit.CAD, 12000), 60, LocalDate.now().minusDays(20));
        Loan[] loans = {cc1,cc2,al1,al2};
        System.out.println("largest loan " + findLargest(loans));

        // Array variance
        CreditCard[] creditCards = {cc1,cc2};

        Loan[] creditCardLoans = creditCards;

        // BOOM ArrayStoreException a runtime error because arrays are covariant
        //creditCardLoans[1] = al1;

        // Can we find the largest of a credit card array?
        // Yes because arrays are covariant
        System.out.println("largest loan " + findLargest(creditCards));

        // How about with a list?
        List<Loan> loansList = Arrays.asList(loans);
        System.out.println("largest loan in list " + findLargestList(loansList)); // works fine

        // Question, can you pass a super class?
        // TODO in order to check that you'd need to create a superclass of Loan

        // With a list of credit cards we find a different issue, credit card list is not a subtype
        // of loan list...

        List<CreditCard> creditCardList = Arrays.asList(creditCards);
        //System.out.println("largest loan in credit card list " + findLargestList(creditCardList)); // compile error

        // But can we just assign the credit cards to a loan list?
        // List<Loan> assignedLoanList = creditCardList; // NOPE wrong type

        // So this is our first encounter with variance, with generic collections we can't get the runtime
        // error of arrays. In fact the problem is worse with generics because of type erasure. Arrays
        // are reified so we would just do something bad and it is not allowed.

        // OK So how do we make it work, specify variance!

        System.out.println("largest loan in credit card list " + findLargestListFixed(creditCardList)); // compile error

        // Get/put principal (can come later).

        // look into filtering by client id into a list of loan from a sub type list of credit cards

        // Works nicely with loans input and loans output...
        List<Loan> clientLoans = new ArrayList<>();
        getClientLoans(1L, loansList, clientLoans);
        System.out.println("clientLoans " + clientLoans);

        // Works nicely with credit cards input and loans output...
        List<Loan> clientLoans2 = new ArrayList<>();
        getClientLoans(1L, creditCardList, clientLoans2);
        System.out.println("clientLoans " + clientLoans2);

        // One thing we can't do is produce a list of credit cards
        // because the output list is invariant
        List<CreditCard> clientLoans3 = new ArrayList<>();
        //getClientLoans(1L, creditCardList, clientLoans3); // required List<? super Loan>

        // Which is fine because it could lead to trouble, but we might
        // want to do something like this...
        List<FinancialInstrument> fis = new ArrayList<>();
        getClientLoans(1L, loansList, fis);
        System.out.println("financial instruments " + fis);

        // Copy all credit cards to a list of loans
        List<Loan> loanListCopied = new ArrayList<>();
        loanListCopied.addAll(creditCardList); // Get
        // This needs Put for the predicate
        loanListCopied.removeIf(l -> l.getPrincipal().isLessThan(Money.of(CurrencyUnit.CAD, 300)));

        List<Number> nums2 = new ArrayList<Number>();
        List<? super Number> sink = nums2;
        List<? extends Number> source = nums2;

        for (int i=0; i<10; i++)
            sink.add(i);

        double sum=0; for (Number num : source)
        sum += num.doubleValue();

        // Wildcard capture

        // list reverse
        List<Integer> l1 = new ArrayList(Arrays.asList(1,2,3,4,5,6));
        reverse1(l1);
        System.out.println(l1);

        List<Integer> l2 = new ArrayList(Arrays.asList(1,2,3,4,5,6));
        reverse2(l2);
        System.out.println(l2);

        // Collections (notes from chapter 13 of Java Generics and Collections)

        EnumSet<DieRolls> es = EnumSet.of(DieRolls.One, DieRolls.Two,DieRolls.Three);
        System.out.println(es);

        // Variance related api
        // No matter the type of the enum you can check for any Object
        assert es.contains(DieRolls.Two);
        assert es.contains(1) == false;

        DieRolls dr1 = DieRolls.One;

        //  boolean removeIf(Predicate<? super E> filter)
        assert es.removeIf(((DieRolls d) -> d == dr1));

        // Temp hashset API for loans

        // credit card hashset
        HashSet<CreditCard> hscc = new HashSet<>();
        hscc.add(cc1);
        hscc.add(cc2);
        // Remove if loan with zero balance - note it is contravariant
        hscc.removeIf((Loan l) -> l.getPrincipal() == Money.zero(CurrencyUnit.CAD));

        HashSet<Loan> hsLoans = new HashSet<>();
        hsLoans.add(cc1);
        hsLoans.add(cc2);
        hsLoans.add(al1);
        hsLoans.add(al2);

        // Can we remove a credit card and if so why?
        // OK it kind of makes sense because... the predicate function must be a super type
        // of the collection type which means either a loan or a financial instrument
        // in other words to PUT (i.e., modify) you must be able to sub something

        // hsLoans.removeIf((CreditCard cc) -> cc.getPrincipal() > Money.zero(CurrencyUnit.CAD)); // Cannot compile
        hsLoans.removeIf((FinancialInstrument fi) -> fi.getClientId() == 0L); // OK

        // In short, the removeIf parameter is contravariant in the collection type. What do we know about each
        // element? Only that it is type E or below... that means that we can substitute either an E or anything
        // above E, since those are the types that we have a known interface for at this point.

        // addAll is covariant GET extends...
        // Collection<? extends E> c)
        // Rationale? Because the collection is only read, and we can put anything into our collection
        // of type E if it is E or below...
        hsLoans.addAll(loansList);
        hsLoans.addAll(creditCardList);

        // We couldn't add a list of financial instruments though
        List<FinancialInstrument> fiList = new ArrayList<>();
        //hsLoans.addAll(fiList);

        // If we could, then we could ruin somebody's day by putting something above Loan into a Loan or below

        // Subtyping from Java spec
        // numbers...

        // char and short are subtype of int
        char c1 = '#';
        short s1 = 20;
        byte b1 = 127;
        int c1i = c1; // this is fine
        int csi = s1;
        double d1 = 20f; // ok because float <1 double
        int i1 = 3256;

        // behaviour of array of primitives
        // is ok to put subtype primitives in an array
        int [] ia1 = {c1,s1,b1};

        // Can't put an int in a short array
        //short [] sa1 = {i1,s1,b1};

        // subtyping among Array types
        // byte < short < int
        // Not among primitives
        byte [] ba1 = {1,2,3};
        // short [] sa1 = ba1; // incompatible types

        Byte [] br1 = {1,2,3};
        //Short [] sr1 = br1; // also incompatible, it is a class and Byte does not extend it

        // but yes with reference types
        CreditCard [] ccArray1 = {cc1,cc2};
        Loan [] loanArray1 = ccArray1; // proper supertype
        FinancialInstrument [] fiArray1 = ccArray1; // transitive supertype
        Object [] oArray1 = ccArray1; // can keep going up
        Object o1 = ccArray1; // this is ok too

        // Application: A priority queue loans by risk

        PriorityQueue<Loan> priorityLoans = new PriorityQueue<>(new CompareLoanOverdueDays());
        List<AmortizedLoan> amloans = Arrays.asList(al1,al2,al3);
        List<CreditCard> ccloans = Arrays.asList(cc1,cc2);

        priorityLoans.addAll(amloans);
        priorityLoans.addAll(ccloans);

        // Can iterate over it but that doesn't get the sorted elements or change it
        for(Loan loan: priorityLoans) {
            System.out.println(loan);
        }

        // Collect the elements into a List sorted by priority
        List<FinancialInstrument> fiOut = new ArrayList();
        while(priorityLoans.size() > 0) {
            Loan l = priorityLoans.poll();
            fiOut.add(l);
        }

        // Now the list is sorted
        for(FinancialInstrument loan: fiOut) {
            System.out.println(loan);
        }

        // Super/Put Contravariance example

        // Print reports for a list of things
        // Because hsLoans HashSet<Loan> is a collection of a subtype of FinancialInstrument this is fine

        FinancialInstrumentReportPrinter financialInstrumentReportPrinter = new FinancialInstrumentReportPrinter();
        LoanReportPrinter creditCardReportPrinter = new LoanReportPrinter();

        FinancialInstrument.printReports(hsLoans, financialInstrumentReportPrinter);

        // We can also pass a credit card list
        // Why is this ok?
        // creditCardList has type List<T> and financialInstrumentReportPrinter prints type T
        FinancialInstrument.printReports(creditCardList, financialInstrumentReportPrinter);

        // This is fine
        FinancialInstrument.printReports(creditCardList, creditCardReportPrinter);

        // So is this
        creditCardReports(creditCardList, creditCardReportPrinter);

        // But if we don't have credit reporter?
        // This works because of the contravariant annotation on the function
        creditCardReports(creditCardList, financialInstrumentReportPrinter);
    }
}
