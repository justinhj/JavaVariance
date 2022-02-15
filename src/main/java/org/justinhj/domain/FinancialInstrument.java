package org.justinhj.domain;

abstract public class FinancialInstrument {
    public Long clientId;

    public FinancialInstrument(Long clientId) {
        this.clientId = clientId;
    }
}
