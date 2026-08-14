package org.demo.sharemgmt.service.model;

import java.math.BigDecimal;

public class PortfolioPosition {

    private final Long shareholderId;
    private final String shareholderName;
    private final String symbol;
    private final int quantity;
    private final BigDecimal investedAmount;
    private final BigDecimal currentPrice;
    private final BigDecimal previousClose;
    private final BigDecimal priceChange;
    private final BigDecimal priceChangePercent;
    private final boolean livePrice;
    private final String priceSource;
    private final BigDecimal marketValue;
    private final BigDecimal gainLoss;
    private final int gainLossPercentage;

    public PortfolioPosition(
        Long shareholderId,
        String shareholderName,
        String symbol,
        int quantity,
        BigDecimal investedAmount,
        BigDecimal currentPrice,
        BigDecimal previousClose,
        BigDecimal priceChange,
        BigDecimal priceChangePercent,
        boolean livePrice,
        String priceSource,
        BigDecimal marketValue,
        BigDecimal gainLoss,
        int gainLossPercentage
    ) {
        this.shareholderId = shareholderId;
        this.shareholderName = shareholderName;
        this.symbol = symbol;
        this.quantity = quantity;
        this.investedAmount = investedAmount;
        this.currentPrice = currentPrice;
        this.previousClose = previousClose;
        this.priceChange = priceChange;
        this.priceChangePercent = priceChangePercent;
        this.livePrice = livePrice;
        this.priceSource = priceSource;
        this.marketValue = marketValue;
        this.gainLoss = gainLoss;
        this.gainLossPercentage = gainLossPercentage;
    }

    public Long getShareholderId() {
        return shareholderId;
    }

    public String getShareholderName() {
        return shareholderName;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public BigDecimal getPreviousClose() {
        return previousClose;
    }

    public BigDecimal getPriceChange() {
        return priceChange;
    }

    public BigDecimal getPriceChangePercent() {
        return priceChangePercent;
    }

    public boolean isLivePrice() {
        return livePrice;
    }

    public String getPriceSource() {
        return priceSource;
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
