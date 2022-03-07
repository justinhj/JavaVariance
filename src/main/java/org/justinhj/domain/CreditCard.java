package org.justinhj.domain;

import org.joda.money.Money;

import java.math.RoundingMode;
import java.time.LocalDate;

public class CreditCard extends Loan {

    final private Money balance;
    final private float interestRate;

    // APR = (((Fees + Interest)/Principal * n) * 365) * 100
    // Where interest is interest paid over the term
    // and n is the number of days in the term

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

    @Override
    public long getOverduePaymentDays() {
        // TODO would be based on last payment date, let's just return since start of month
        return LocalDate.now().getDayOfMonth();
    }

    public CreditCard(Long loanId, Long clientId, float interestRate, Money openingBalance) {
        super(loanId, clientId);
        this.clientId = clientId;
        this.loanId = loanId;
        this.interestRate = interestRate;
        balance = openingBalance;
    }
}
