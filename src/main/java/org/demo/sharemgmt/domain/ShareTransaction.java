package org.demo.sharemgmt.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "share_transactions")
public class ShareTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shareholder_id", nullable = false)
    private Shareholder shareholder;

    @Column(nullable = false, length = 16)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private TransactionType transactionType;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerShare;

    @Column(nullable = false)
    private LocalDate transactionDate;

    protected ShareTransaction() {
    }

    public ShareTransaction(
        Shareholder shareholder,
        String symbol,
        TransactionType transactionType,
        int quantity,
        BigDecimal pricePerShare,
        LocalDate transactionDate
    ) {
        this.shareholder = shareholder;
        this.symbol = symbol;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.transactionDate = transactionDate;
    }

    public Long getId() {
        return id;
    }

    public Shareholder getShareholder() {
        return shareholder;
    }

    public String getSymbol() {
        return symbol;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPricePerShare() {
        return pricePerShare;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public BigDecimal getGrossAmount() {
        return pricePerShare.multiply(BigDecimal.valueOf(quantity));
    }
}
