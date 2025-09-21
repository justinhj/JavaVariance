package org.justinhj.util;

import org.justinhj.domain.Loan;

public class LoanReportPrinter implements ReportPrinter<Loan> {
    @Override
    public void report(Loan reportItem) {
        System.out.println("[Loan " +
                reportItem.getClientId().toString() +
                ", " +
                reportItem.getPrincipal() +
                "]");
    }
}
