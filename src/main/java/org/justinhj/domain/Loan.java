package org.justinhj.domain;

import org.joda.money.Money;

// Types of loan repayment
//   interest-only - you pay accrual of interest and not the principal
//   amortizing - you pay principal and interest over a set period
//   credit card loan - you pay interest on total amount of purchases
//     after a fixed period
//     For example you spend 300 in March and you have one-month interest-free period
//     at the end of March you don't pay your bill, you have no interest due though
//     during April you spend 200. At the end of April you owe $500 + interest on the
//     300, let's say it's 1% per month, so you owe $5
//     You pay $100 so now your balance is 500 + 5 - 100 = 405
//     Internally you must keep two totals:
//        Sum of Individual items that are interest free (amount, purchase date)
//        Aggregate sum of items no longer free (no longer track individually)
//
// Calculating periodic payments
//    Payment (P) Loan amount, principal (a), Periodic interest rate, annual rate divided by periods (r)
//    and total payments, gives the duration of the loan (n)

// What is common to all loans? Principal amount remaining,

// Credit cards can advertise a month rate but must also disclose the APR
// It includes (some) fees as well as the principal
    // APR = (((Fees + Interest)/Principal * n) * 365) * 100
    // Where interest is interest paid over the term
    // and n is the number of days in the term



public abstract class Loan extends FinancialInstrument {

    public abstract Money getPrincipal();
    public abstract float getInterestRate();
    public abstract Money getMonthlyPayment();

    public Long loanId;

    @Override
    public String toString() {
        return "Loan{" +
                "clientId=" + clientId +
                ", loanId=" + loanId +
                ", principal=" + getPrincipal() +
                ", rate=" + getInterestRate() + "%" +
                '}';
    }

    public Loan(Long loanId, Long clientId) {
        super(clientId);
        this.loanId = loanId;
    }
}

