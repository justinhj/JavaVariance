package org.justinhj.domain;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.time.LocalDate;

/**
 * An Amortized Loan has a fixed number of monthly payments
 * where the principal and the interest is paid off and at the
 * end of the term the balance is zero.
 */
public class AmortizedLoan extends Loan {
    public float interestRate;

    @Override
    public Money getPrincipal() {
        // TODO
        return Money.of(CurrencyUnit.CAD, 10f);
    }

    @Override
    public float getInterestRate() {
        return 0;
    }

    @Override
    public Money getMonthlyPayment() {
        // TODO
        return Money.of(CurrencyUnit.CAD, 10);
    }

    public AmortizedLoan(Long loanId, Long clientId, float interestRate, Money principal, int months, LocalDate start) {
        super(loanId, clientId);
        this.interestRate = interestRate;
    }
}
