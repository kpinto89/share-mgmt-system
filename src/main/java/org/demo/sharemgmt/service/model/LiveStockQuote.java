package org.demo.sharemgmt.service.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class LiveStockQuote {

    private final String symbol;
    private final BigDecimal currentPrice;
    private final BigDecimal previousClose;
    private final BigDecimal change;
    private final BigDecimal changePercent;
    private final boolean live;
    private final String source;
    private final OffsetDateTime retrievedAt;

    private LiveStockQuote(
        String symbol,
        BigDecimal currentPrice,
        BigDecimal previousClose,
        BigDecimal change,
        BigDecimal changePercent,
        boolean live,
        String source,
        OffsetDateTime retrievedAt
    ) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.previousClose = previousClose;
        this.change = change;
        this.changePercent = changePercent;
        this.live = live;
        this.source = source;
        this.retrievedAt = retrievedAt;
    }

    public static LiveStockQuote live(
        String symbol,
        BigDecimal currentPrice,
        BigDecimal previousClose,
        BigDecimal change,
        BigDecimal changePercent,
        OffsetDateTime retrievedAt
    ) {
        return new LiveStockQuote(symbol, currentPrice, previousClose, change, changePercent, true, "Alpha Vantage", retrievedAt);
    }

    public static LiveStockQuote fallback(String symbol, BigDecimal currentPrice, String source) {
        return new LiveStockQuote(symbol, currentPrice, currentPrice, BigDecimal.ZERO, BigDecimal.ZERO, false, source, OffsetDateTime.now());
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public BigDecimal getPreviousClose() {
        return previousClose;
    }

    public BigDecimal getChange() {
        return change;
    }

    public BigDecimal getChangePercent() {
        return changePercent;
    }

    public boolean isLive() {
        return live;
    }

    public String getSource() {
        return source;
    }

    public OffsetDateTime getRetrievedAt() {
        return retrievedAt;
    }
}
