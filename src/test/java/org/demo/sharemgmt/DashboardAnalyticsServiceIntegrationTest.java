package org.demo.sharemgmt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.demo.sharemgmt.domain.Shareholder;
import org.demo.sharemgmt.domain.TransactionType;
import org.demo.sharemgmt.repository.ShareTransactionRepository;
import org.demo.sharemgmt.repository.ShareholderRepository;
import org.demo.sharemgmt.service.DashboardAnalyticsService;
import org.demo.sharemgmt.service.ShareTransactionService;
import org.demo.sharemgmt.service.model.ChartBarItem;
import org.demo.sharemgmt.service.model.ChartColumnItem;
import org.demo.sharemgmt.service.model.DashboardAnalytics;
import org.demo.sharemgmt.web.form.ShareTransactionForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DashboardAnalyticsServiceIntegrationTest {

    @Autowired
    private ShareholderRepository shareholderRepository;

    @Autowired
    private ShareTransactionRepository shareTransactionRepository;

    @Autowired
    private ShareTransactionService shareTransactionService;

    @Autowired
    private DashboardAnalyticsService dashboardAnalyticsService;

    @BeforeEach
    void setUp() {
        shareTransactionRepository.deleteAll();
        shareholderRepository.deleteAll();
    }

    @Test
    void aggregatesChartDataFromRecordedTransactions() {
        Shareholder anika = shareholderRepository.save(new Shareholder("Anika Sharma", "anika@test.local"));
        Shareholder ravi = shareholderRepository.save(new Shareholder("Ravi Menon", "ravi@test.local"));

        shareTransactionService.recordTransaction(buildForm(anika.getId(), "TCS", TransactionType.BUY, 10, "100.00", LocalDate.of(2026, 8, 10)));
        shareTransactionService.recordTransaction(buildForm(anika.getId(), "TCS", TransactionType.SELL, 4, "120.00", LocalDate.of(2026, 8, 11)));
        shareTransactionService.recordTransaction(buildForm(ravi.getId(), "INFY", TransactionType.BUY, 8, "200.00", LocalDate.of(2026, 8, 11)));
        shareTransactionService.recordTransaction(buildForm(ravi.getId(), "TCS", TransactionType.BUY, 3, "110.00", LocalDate.of(2026, 8, 12)));

        DashboardAnalytics analytics = dashboardAnalyticsService.getDashboardAnalytics();

        assertEquals(List.of("TCS", "INFY"), labels(analytics.getOwnedSharesBySymbol()));
        assertEquals(List.of("9 shares", "8 shares"), displayValues(analytics.getOwnedSharesBySymbol()));
        assertEquals(List.of("Ravi Menon", "Anika Sharma"), labels(analytics.getInvestmentByShareholder()));
        assertEquals(List.of("INR 1930.00", "INR 1000.00"), displayValues(analytics.getInvestmentByShareholder()));
        assertEquals(List.of("10 Aug", "11 Aug", "12 Aug"), activityLabels(analytics.getTransactionActivity()));
        assertEquals(List.of(1, 2, 1), activityCounts(analytics.getTransactionActivity()));
    }

    private List<String> labels(List<ChartBarItem> items) {
        return items.stream().map(ChartBarItem::getLabel).collect(Collectors.toList());
    }

    private List<String> displayValues(List<ChartBarItem> items) {
        return items.stream().map(ChartBarItem::getDisplayValue).collect(Collectors.toList());
    }

    private List<String> activityLabels(List<ChartColumnItem> items) {
        return items.stream().map(ChartColumnItem::getLabel).collect(Collectors.toList());
    }

    private List<Integer> activityCounts(List<ChartColumnItem> items) {
        return items.stream().map(ChartColumnItem::getCount).collect(Collectors.toList());
    }

    private ShareTransactionForm buildForm(
        Long shareholderId,
        String symbol,
        TransactionType transactionType,
        int quantity,
        String pricePerShare,
        LocalDate transactionDate
    ) {
        ShareTransactionForm form = new ShareTransactionForm();
        form.setShareholderId(shareholderId);
        form.setSymbol(symbol);
        form.setTransactionType(transactionType);
        form.setQuantity(quantity);
        form.setPricePerShare(new BigDecimal(pricePerShare));
        form.setTransactionDate(transactionDate);
        return form;
    }
}
