package org.demo.sharemgmt.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.demo.sharemgmt.domain.ShareTransaction;
import org.demo.sharemgmt.repository.ShareTransactionRepository;
import org.demo.sharemgmt.service.model.LiveStockQuote;
import org.springframework.stereotype.Service;

@Service
public class MarketPriceService {

    private final ShareTransactionRepository shareTransactionRepository;
    private final StockQuoteClient stockQuoteClient;
    private final Map<String, BigDecimal> demoPrices = new HashMap<>();

    public MarketPriceService(ShareTransactionRepository shareTransactionRepository, StockQuoteClient stockQuoteClient) {
        this.shareTransactionRepository = shareTransactionRepository;
        this.stockQuoteClient = stockQuoteClient;
        demoPrices.put("TCS", new BigDecimal("3925.00"));
        demoPrices.put("INFY", new BigDecimal("1615.00"));
        demoPrices.put("RELIANCE", new BigDecimal("3010.00"));
        demoPrices.put("HDFCBANK", new BigDecimal("1748.00"));
        demoPrices.put("SBIN", new BigDecimal("812.00"));
        demoPrices.put("ITC", new BigDecimal("448.00"));
    }

    public BigDecimal getCurrentPrice(String symbol) {
        return getQuote(symbol).getCurrentPrice();
    }

    public LiveStockQuote getQuote(String symbol) {
        String normalized = symbol.toUpperCase(Locale.ROOT);
        return stockQuoteClient.fetchQuote(normalized)
            .orElseGet(() -> fallbackQuote(normalized));
    }

    private LiveStockQuote fallbackQuote(String symbol) {
        BigDecimal price = demoPrices.get(symbol);
        if (price != null) {
            return LiveStockQuote.fallback(symbol, price, "Local fallback price");
        }

        ShareTransaction lastTransaction = shareTransactionRepository.findFirstBySymbolIgnoreCaseOrderByTransactionDateDescIdDesc(symbol);
        if (lastTransaction != null) {
            return LiveStockQuote.fallback(symbol, lastTransaction.getPricePerShare(), "Latest recorded trade");
        }

        return LiveStockQuote.fallback(symbol, BigDecimal.ZERO, "Unavailable");
    }
}
