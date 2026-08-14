package org.demo.sharemgmt.service.model;

import java.math.BigDecimal;

public class HoldingSummary {

    private final Long shareholderId;
    private final String shareholderName;
    private final String symbol;
    private int purchasedQuantity;
    private int soldQuantity;
    private BigDecimal investedAmount = BigDecimal.ZERO;
    private BigDecimal realizedAmount = BigDecimal.ZERO;

    public HoldingSummary(Long shareholderId, String shareholderName, String symbol) {
        this.shareholderId = shareholderId;
        this.shareholderName = shareholderName;
        this.symbol = symbol;
    }

    public void addPurchase(int quantity, BigDecimal amount) {
        purchasedQuantity += quantity;
        investedAmount = investedAmount.add(amount);
    }

    public void addSale(int quantity, BigDecimal amount) {
        soldQuantity += quantity;
        realizedAmount = realizedAmount.add(amount);
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

    public int getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public int getOwnedQuantity() {
        return purchasedQuantity - soldQuantity;
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public BigDecimal getRealizedAmount() {
        return realizedAmount;
    }
}
