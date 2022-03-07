package org.justinhj.util;

import org.justinhj.domain.CreditCard;
import org.justinhj.domain.FinancialInstrument;

public class CreditCardReportPrinter implements ReportPrinter<CreditCard> {
    @Override
    public void report(CreditCard reportItem) {
        System.out.println("[Credit Card, " +
                reportItem.getClientId().toString() +
                ", " +
                reportItem.getPrincipal() +
                "]");
    }
}
