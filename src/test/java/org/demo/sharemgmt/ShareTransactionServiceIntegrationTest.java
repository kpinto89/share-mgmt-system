package org.demo.sharemgmt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.demo.sharemgmt.domain.Shareholder;
import org.demo.sharemgmt.domain.TransactionType;
import org.demo.sharemgmt.repository.ShareTransactionRepository;
import org.demo.sharemgmt.repository.ShareholderRepository;
import org.demo.sharemgmt.service.PortfolioService;
import org.demo.sharemgmt.service.ShareTransactionService;
import org.demo.sharemgmt.service.model.HoldingSummary;
import org.demo.sharemgmt.web.form.ShareTransactionForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ShareTransactionServiceIntegrationTest {

    @Autowired
    private ShareholderRepository shareholderRepository;

    @Autowired
    private ShareTransactionRepository shareTransactionRepository;

    @Autowired
    private ShareTransactionService shareTransactionService;

    @Autowired
    private PortfolioService portfolioService;

    @BeforeEach
    void setUp() {
        shareTransactionRepository.deleteAll();
        shareholderRepository.deleteAll();
    }

    @Test
    void recordsBuyTransactionsAndSummarizesHoldings() {
        Shareholder shareholder = shareholderRepository.save(new Shareholder("Demo Holder", "holder@test.local"));

        shareTransactionService.recordTransaction(buildForm(shareholder.getId(), "INFY", TransactionType.BUY, 10, "1550.00"));
        shareTransactionService.recordTransaction(buildForm(shareholder.getId(), "INFY", TransactionType.SELL, 3, "1610.00"));

        List<HoldingSummary> holdings = portfolioService.getHoldingSummaries();

        assertEquals(1, holdings.size());
        assertEquals(7, holdings.get(0).getOwnedQuantity());
        assertEquals(new BigDecimal("15500.00"), holdings.get(0).getInvestedAmount());
        assertEquals(new BigDecimal("4830.00"), holdings.get(0).getRealizedAmount());
    }

    @Test
    void rejectsSellTransactionWhenHoldingIsInsufficient() {
        Shareholder shareholder = shareholderRepository.save(new Shareholder("Demo Holder", "holder@test.local"));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> shareTransactionService.recordTransaction(buildForm(shareholder.getId(), "TCS", TransactionType.SELL, 2, "3710.00"))
        );

        assertEquals(
            "Cannot sell 2 shares of TCS because only 0 are currently owned.",
            exception.getMessage()
        );
    }

    private ShareTransactionForm buildForm(
        Long shareholderId,
        String symbol,
        TransactionType transactionType,
        int quantity,
        String pricePerShare
    ) {
        ShareTransactionForm form = new ShareTransactionForm();
        form.setShareholderId(shareholderId);
        form.setSymbol(symbol);
        form.setTransactionType(transactionType);
        form.setQuantity(quantity);
        form.setPricePerShare(new BigDecimal(pricePerShare));
        form.setTransactionDate(LocalDate.now());
        return form;
    }
}
