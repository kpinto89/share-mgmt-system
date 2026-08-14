package org.demo.sharemgmt.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.demo.sharemgmt.domain.ShareTransaction;
import org.demo.sharemgmt.domain.TransactionType;
import org.demo.sharemgmt.repository.ShareTransactionRepository;
import org.demo.sharemgmt.service.model.HoldingSummary;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    private final ShareTransactionRepository shareTransactionRepository;

    public PortfolioService(ShareTransactionRepository shareTransactionRepository) {
        this.shareTransactionRepository = shareTransactionRepository;
    }

    public List<HoldingSummary> getHoldingSummaries() {
        Map<String, HoldingSummary> summaries = new LinkedHashMap<>();
        for (ShareTransaction transaction : shareTransactionRepository.findAllByOrderByTransactionDateDescIdDesc()) {
            String key = transaction.getShareholder().getId() + "|" + transaction.getSymbol().toUpperCase();
            HoldingSummary summary = summaries.computeIfAbsent(
                key,
                ignored -> new HoldingSummary(
                    transaction.getShareholder().getId(),
                    transaction.getShareholder().getName(),
                    transaction.getSymbol().toUpperCase()
                )
            );
            if (transaction.getTransactionType() == TransactionType.BUY) {
                summary.addPurchase(transaction.getQuantity(), transaction.getGrossAmount());
            } else {
                summary.addSale(transaction.getQuantity(), transaction.getGrossAmount());
            }
        }
        List<HoldingSummary> result = new ArrayList<>(summaries.values());
        result.sort(
            Comparator.comparing(HoldingSummary::getShareholderName)
                .thenComparing(HoldingSummary::getSymbol)
        );
        return result;
    }

    public int getCurrentHoldingQuantity(Long shareholderId, String symbol) {
        int quantity = 0;
        for (ShareTransaction transaction : shareTransactionRepository.findByShareholderIdAndSymbolIgnoreCaseOrderByTransactionDateAscIdAsc(
            shareholderId,
            symbol
        )) {
            quantity += transaction.getTransactionType() == TransactionType.BUY
                ? transaction.getQuantity()
                : -transaction.getQuantity();
        }
        return quantity;
    }

    public long getTransactionCount() {
        return shareTransactionRepository.count();
    }

    public int getTotalOwnedShareCount() {
        return getHoldingSummaries().stream()
            .mapToInt(HoldingSummary::getOwnedQuantity)
            .sum();
    }

    public BigDecimal getTotalInvestment() {
        return getHoldingSummaries().stream()
            .map(HoldingSummary::getInvestedAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ShareTransaction> getRecentTransactions() {
        return shareTransactionRepository.findTop10ByOrderByTransactionDateDescIdDesc();
    }
}
