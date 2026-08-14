package org.demo.sharemgmt.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.demo.sharemgmt.domain.ShareTransaction;
import org.demo.sharemgmt.repository.ShareTransactionRepository;
import org.demo.sharemgmt.service.model.ChartBarItem;
import org.demo.sharemgmt.service.model.ChartColumnItem;
import org.demo.sharemgmt.service.model.DashboardAnalytics;
import org.demo.sharemgmt.service.model.HoldingSummary;
import org.springframework.stereotype.Service;

@Service
public class DashboardAnalyticsService {

    private static final DateTimeFormatter DAY_LABEL_FORMAT = DateTimeFormatter.ofPattern("dd MMM");

    private final PortfolioService portfolioService;
    private final ShareTransactionRepository shareTransactionRepository;

    public DashboardAnalyticsService(
        PortfolioService portfolioService,
        ShareTransactionRepository shareTransactionRepository
    ) {
        this.portfolioService = portfolioService;
        this.shareTransactionRepository = shareTransactionRepository;
    }

    public DashboardAnalytics getDashboardAnalytics() {
        List<HoldingSummary> holdings = portfolioService.getHoldingSummaries();
        return new DashboardAnalytics(
            buildOwnedSharesBySymbol(holdings),
            buildInvestmentByShareholder(holdings),
            buildTransactionActivity(shareTransactionRepository.findAllByOrderByTransactionDateDescIdDesc())
        );
    }

    private List<ChartBarItem> buildOwnedSharesBySymbol(List<HoldingSummary> holdings) {
        Map<String, Integer> ownedBySymbol = new LinkedHashMap<>();
        for (HoldingSummary holding : holdings) {
            ownedBySymbol.merge(holding.getSymbol(), holding.getOwnedQuantity(), Integer::sum);
        }

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(ownedBySymbol.entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry::getKey));

        int max = entries.stream().mapToInt(Map.Entry::getValue).max().orElse(0);
        List<ChartBarItem> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entries) {
            BigDecimal value = BigDecimal.valueOf(entry.getValue());
            result.add(new ChartBarItem(entry.getKey(), value, entry.getValue() + " shares", percentage(value, BigDecimal.valueOf(max))));
        }
        return result;
    }

    private List<ChartBarItem> buildInvestmentByShareholder(List<HoldingSummary> holdings) {
        Map<String, BigDecimal> investmentByShareholder = new LinkedHashMap<>();
        for (HoldingSummary holding : holdings) {
            investmentByShareholder.merge(holding.getShareholderName(), holding.getInvestedAmount(), BigDecimal::add);
        }

        List<Map.Entry<String, BigDecimal>> entries = new ArrayList<>(investmentByShareholder.entrySet());
        entries.sort(Map.Entry.<String, BigDecimal>comparingByValue().reversed().thenComparing(Map.Entry::getKey));

        BigDecimal max = entries.stream().map(Map.Entry::getValue).max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
        List<ChartBarItem> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : entries) {
            result.add(
                new ChartBarItem(
                    entry.getKey(),
                    entry.getValue(),
                    "INR " + formatMoney(entry.getValue()),
                    percentage(entry.getValue(), max)
                )
            );
        }
        return result;
    }

    private List<ChartColumnItem> buildTransactionActivity(List<ShareTransaction> transactions) {
        Map<String, Integer> activityByDay = new LinkedHashMap<>();
        transactions.stream()
            .sorted(Comparator.comparing(ShareTransaction::getTransactionDate).thenComparing(ShareTransaction::getId))
            .forEach(transaction -> activityByDay.merge(transaction.getTransactionDate().format(DAY_LABEL_FORMAT), 1, Integer::sum));

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(activityByDay.entrySet());
        if (entries.size() > 7) {
            entries = entries.subList(entries.size() - 7, entries.size());
        }

        int max = entries.stream().mapToInt(Map.Entry::getValue).max().orElse(0);
        List<ChartColumnItem> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entries) {
            result.add(new ChartColumnItem(entry.getKey(), entry.getValue(), percentage(BigDecimal.valueOf(entry.getValue()), BigDecimal.valueOf(max))));
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

    private String formatMoney(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
