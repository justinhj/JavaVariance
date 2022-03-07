package org.justinhj.domain;

import org.justinhj.util.ReportPrinter;

import java.util.Collection;

abstract public class FinancialInstrument {
    protected Long clientId;

    public Long getClientId() {
        return clientId;
    }

    public FinancialInstrument(Long clientId) {
        this.clientId = clientId;
    }

    public static <T> void printReports(Collection<? extends T> reports, ReportPrinter<T> reportPrinter) {
        for (T report : reports) {
            reportPrinter.report(report);
        }
    }

}
