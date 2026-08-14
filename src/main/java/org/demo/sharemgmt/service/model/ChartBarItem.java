package org.demo.sharemgmt.service.model;

import java.math.BigDecimal;

public class ChartBarItem {

    private final String label;
    private final BigDecimal value;
    private final String displayValue;
    private final int percentage;

    public ChartBarItem(String label, BigDecimal value, String displayValue, int percentage) {
        this.label = label;
        this.value = value;
        this.displayValue = displayValue;
        this.percentage = percentage;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public int getPercentage() {
        return percentage;
    }
}
