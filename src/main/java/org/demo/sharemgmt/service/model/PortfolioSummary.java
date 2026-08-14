package org.demo.sharemgmt.service.model;

import java.math.BigDecimal;

public class PortfolioSummary {

    private final int totalShares;
    private final BigDecimal investedAmount;
    private final BigDecimal marketValue;
    private final BigDecimal gainLoss;
    private final int gainLossPercentage;

    public PortfolioSummary(
        int totalShares,
        BigDecimal investedAmount,
        BigDecimal marketValue,
        BigDecimal gainLoss,
        int gainLossPercentage
    ) {
        this.totalShares = totalShares;
        this.investedAmount = investedAmount;
        this.marketValue = marketValue;
        this.gainLoss = gainLoss;
        this.gainLossPercentage = gainLossPercentage;
    }

    public int getTotalShares() {
        return totalShares;
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public BigDecimal getGainLoss() {
        return gainLoss;
    }

    public int getGainLossPercentage() {
        return gainLossPercentage;
    }

    public boolean isGain() {
        return gainLoss.signum() >= 0;
    }
}
