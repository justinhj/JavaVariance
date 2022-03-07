package org.justinhj.util;

import org.justinhj.domain.FinancialInstrument;

public class FinancialInstrumentReportPrinter implements ReportPrinter<FinancialInstrument> {

    @Override
    public void report(FinancialInstrument reportItem) {
        System.out.println("[Financial Instrument, " + reportItem.getClientId().toString() + "]");
    }
}
