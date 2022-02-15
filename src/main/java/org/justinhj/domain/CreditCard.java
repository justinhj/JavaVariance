package org.justinhj.domain;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.RoundingMode;

public class CreditCard extends Loan {

    private Money balance;
    private float interestRate;

    // APR = (((Fees + Interest)/Principal * n) * 365) * 100
    // Where interest is interest paid over the term
    // and n is the number of days in the term

    /**
     * TODO this garbage
     * @param annualFee Any fees on top of interest payments as an annual total
     * @param interestRate Annual simple interest rate not considering compounding
     * @return
     */
    public static float calculateAPR(Money annualFee, float interestRate) {
        float principal = 100.0f;
        float n = 365.0f;
        float dailyInterestRate = interestRate / 365.0f;
        float fee = annualFee.getAmountMinorInt() / 100.0f; // TODO probably not safe for all currencies
        float feePlusInterest = fee + (principal * (dailyInterestRate / n));
        float apr = ((feePlusInterest / principal) / n) * 365.0f;
        return apr;
    }

    @Override
    public Money getPrincipal() {
        return balance;
    }

    @Override
    public float getInterestRate() {
        return interestRate;
    }

    @Override
    public Money getMonthlyPayment() {
        return balance.multipliedBy(interestRate, RoundingMode.FLOOR);
    }

    public CreditCard(Long loanId, Long clientId, float interestRate, Money openingBalance) {
        super(loanId, clientId);
        this.clientId = clientId;
        this.loanId = loanId;
        this.interestRate = interestRate;
        balance = openingBalance;
    }
}
