package org.demo.sharemgmt;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.demo.sharemgmt.domain.Shareholder;
import org.demo.sharemgmt.domain.TransactionType;
import org.demo.sharemgmt.repository.ShareTransactionRepository;
import org.demo.sharemgmt.repository.ShareholderRepository;
import org.demo.sharemgmt.service.PortfolioService;
import org.demo.sharemgmt.service.StockQuoteClient;
import org.demo.sharemgmt.service.model.LiveStockQuote;
import org.demo.sharemgmt.service.model.PortfolioPosition;
import org.demo.sharemgmt.service.model.PortfolioView;
import org.demo.sharemgmt.web.form.ShareTransactionForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class LivePortfolioTrackingIntegrationTest {

    @Autowired
    private ShareholderRepository shareholderRepository;

    @Autowired
    private ShareTransactionRepository shareTransactionRepository;

    @Autowired
    private PortfolioService portfolioService;

    @MockBean
    private StockQuoteClient stockQuoteClient;

    @BeforeEach
    void setUp() {
        shareTransactionRepository.deleteAll();
        shareholderRepository.deleteAll();
    }

    @Test
    void usesLiveQuotesWhenApiReturnsData() {
        Shareholder shareholder = shareholderRepository.save(new Shareholder("Demo Holder", "holder@test.local"));
        shareTransactionRepository.save(buildTransaction(shareholder, "TCS", TransactionType.BUY, 2, "100.00"));

        when(stockQuoteClient.fetchQuote("TCS")).thenReturn(java.util.Optional.of(
            LiveStockQuote.live(
                "TCS",
                new BigDecimal("4000.00"),
                new BigDecimal("3900.00"),
                new BigDecimal("100.00"),
                new BigDecimal("2.56"),
                OffsetDateTime.now()
            )
        ));

        PortfolioView view = portfolioService.getPortfolioView();
        PortfolioPosition position = view.getPositions().get(0);

        assertTrue(position.isLivePrice());
        assertEquals(new BigDecimal("4000.00"), position.getCurrentPrice());
        assertEquals(new BigDecimal("3900.00"), position.getPreviousClose());
        assertEquals(new BigDecimal("100.00"), position.getPriceChange());
        assertEquals(new BigDecimal("2.56"), position.getPriceChangePercent());
        assertEquals(new BigDecimal("8000.00"), position.getMarketValue());
    }

    private org.demo.sharemgmt.domain.ShareTransaction buildTransaction(
        Shareholder shareholder,
        String symbol,
        TransactionType transactionType,
        int quantity,
        String pricePerShare
    ) {
        return new org.demo.sharemgmt.domain.ShareTransaction(
            shareholder,
            symbol,
            transactionType,
            quantity,
            new BigDecimal(pricePerShare),
            LocalDate.now()
        );
    }
}
