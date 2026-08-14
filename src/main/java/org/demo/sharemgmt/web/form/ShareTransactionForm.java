package org.demo.sharemgmt.web.form;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.demo.sharemgmt.domain.TransactionType;
import org.springframework.format.annotation.DateTimeFormat;

public class ShareTransactionForm {

    @NotNull(message = "Select a shareholder.")
    private Long shareholderId;

    @NotBlank(message = "Share symbol is required.")
    private String symbol;

    @NotNull(message = "Select a transaction type.")
    private TransactionType transactionType;

    @Min(value = 1, message = "Quantity must be at least 1.")
    private int quantity;

    @NotNull(message = "Price per share is required.")
    @DecimalMin(value = "0.01", message = "Price per share must be greater than 0.")
    private BigDecimal pricePerShare;

    @NotNull(message = "Transaction date is required.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate transactionDate;

    public Long getShareholderId() {
        return shareholderId;
    }

    public void setShareholderId(Long shareholderId) {
        this.shareholderId = shareholderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerShare() {
        return pricePerShare;
    }

    public void setPricePerShare(BigDecimal pricePerShare) {
        this.pricePerShare = pricePerShare;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}
