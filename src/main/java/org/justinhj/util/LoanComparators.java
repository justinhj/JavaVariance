package org.justinhj.util;

import java.util.Comparator;

import org.justinhj.domain.Loan;

/**
 * Utility class containing comparators for Loan objects.
 */
public class LoanComparators {

    public static class CompareLoanPrincipal implements Comparator<Loan> {
        @Override
        public int compare(Loan o1, Loan o2) {
            return o1.getPrincipal().compareTo(o2.getPrincipal());
        }
    }

    public static class CompareLoanOverdueDays implements Comparator<Loan> {
        @Override
        public int compare(Loan o1, Loan o2) {
            return Long.compare(o1.getOverduePaymentDays(), o2.getOverduePaymentDays());
        }
    }
}
