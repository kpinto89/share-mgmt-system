package org.demo.sharemgmt.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.demo.sharemgmt.domain.ShareTransaction;
import org.demo.sharemgmt.domain.TransactionType;
import org.demo.sharemgmt.repository.ShareTransactionRepository;
import org.demo.sharemgmt.service.model.ChartBarItem;
import org.demo.sharemgmt.service.model.LiveStockQuote;
import org.demo.sharemgmt.service.model.HoldingSummary;
import org.demo.sharemgmt.service.model.PortfolioPosition;
import org.demo.sharemgmt.service.model.PortfolioSummary;
import org.demo.sharemgmt.service.model.PortfolioView;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    private final ShareTransactionRepository shareTransactionRepository;
    private final MarketPriceService marketPriceService;

    public PortfolioService(ShareTransactionRepository shareTransactionRepository, MarketPriceService marketPriceService) {
        this.shareTransactionRepository = shareTransactionRepository;
        this.marketPriceService = marketPriceService;
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

    public PortfolioView getPortfolioView() {
        List<HoldingSummary> holdings = getHoldingSummaries();
        List<PortfolioPosition> positions = buildPositions(holdings);
        return new PortfolioView(
            buildPortfolioSummary(positions),
            positions,
            buildAllocationBySymbol(positions),
            buildAllocationByShareholder(positions)
        );
    }

    private List<PortfolioPosition> buildPositions(List<HoldingSummary> holdings) {
        List<PortfolioPosition> positions = new ArrayList<>();
        for (HoldingSummary holding : holdings) {
            if (holding.getOwnedQuantity() <= 0) {
                continue;
            }

            LiveStockQuote quote = marketPriceService.getQuote(holding.getSymbol());
            BigDecimal currentPrice = quote.getCurrentPrice();
            BigDecimal marketValue = currentPrice.multiply(BigDecimal.valueOf(holding.getOwnedQuantity()));
            BigDecimal netInvested = holding.getInvestedAmount().subtract(holding.getRealizedAmount());
            BigDecimal gainLoss = marketValue.subtract(netInvested);
            positions.add(
                new PortfolioPosition(
                    holding.getShareholderId(),
                    holding.getShareholderName(),
                    holding.getSymbol(),
                    holding.getOwnedQuantity(),
                    netInvested,
                    currentPrice,
                    quote.getPreviousClose(),
                    quote.getChange(),
                    quote.getChangePercent(),
                    quote.isLive(),
                    quote.getSource(),
                    marketValue,
                    gainLoss,
                    percentage(gainLoss, netInvested)
                )
            );
        }

        positions.sort(
            Comparator.comparing(PortfolioPosition::getMarketValue).reversed()
                .thenComparing(PortfolioPosition::getShareholderName)
                .thenComparing(PortfolioPosition::getSymbol)
        );
        return positions;
    }

    private PortfolioSummary buildPortfolioSummary(List<PortfolioPosition> positions) {
        int totalShares = positions.stream().mapToInt(PortfolioPosition::getQuantity).sum();
        BigDecimal investedAmount = positions.stream().map(PortfolioPosition::getInvestedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal marketValue = positions.stream().map(PortfolioPosition::getMarketValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal gainLoss = marketValue.subtract(investedAmount);
        return new PortfolioSummary(totalShares, investedAmount, marketValue, gainLoss, percentage(gainLoss, investedAmount));
    }

    private List<ChartBarItem> buildAllocationBySymbol(List<PortfolioPosition> positions) {
        Map<String, BigDecimal> totals = new LinkedHashMap<>();
        for (PortfolioPosition position : positions) {
            totals.merge(position.getSymbol(), position.getMarketValue(), BigDecimal::add);
        }
        return buildAllocationChart(totals);
    }

    private List<ChartBarItem> buildAllocationByShareholder(List<PortfolioPosition> positions) {
        Map<String, BigDecimal> totals = new LinkedHashMap<>();
        for (PortfolioPosition position : positions) {
            totals.merge(position.getShareholderName(), position.getMarketValue(), BigDecimal::add);
        }
        return buildAllocationChart(totals);
    }

    private List<ChartBarItem> buildAllocationChart(Map<String, BigDecimal> totals) {
        List<Map.Entry<String, BigDecimal>> entries = new ArrayList<>(totals.entrySet());
        entries.sort(Map.Entry.<String, BigDecimal>comparingByValue().reversed().thenComparing(Map.Entry::getKey));

        BigDecimal max = entries.stream().map(Map.Entry::getValue).max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
        List<ChartBarItem> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : entries) {
            result.add(
                new ChartBarItem(
                    entry.getKey(),
                    entry.getValue(),
                    "INR " + entry.getValue().setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    percentage(entry.getValue(), max)
                )
            );
        }
        return result;
    }

    private int percentage(BigDecimal value, BigDecimal max) {
        if (max.signum() <= 0 || value.signum() <= 0) {
            return 0;
        }
        return value.multiply(BigDecimal.valueOf(100))
            .divide(max, 0, RoundingMode.HALF_UP)
            .intValue();
    }
}
