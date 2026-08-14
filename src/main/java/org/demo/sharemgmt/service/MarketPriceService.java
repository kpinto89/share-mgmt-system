package org.demo.sharemgmt.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.demo.sharemgmt.domain.ShareTransaction;
import org.demo.sharemgmt.repository.ShareTransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class MarketPriceService {

    private final ShareTransactionRepository shareTransactionRepository;
    private final Map<String, BigDecimal> demoPrices = new HashMap<>();

    public MarketPriceService(ShareTransactionRepository shareTransactionRepository) {
        this.shareTransactionRepository = shareTransactionRepository;
        demoPrices.put("TCS", new BigDecimal("3925.00"));
        demoPrices.put("INFY", new BigDecimal("1615.00"));
        demoPrices.put("RELIANCE", new BigDecimal("3010.00"));
        demoPrices.put("HDFCBANK", new BigDecimal("1748.00"));
        demoPrices.put("SBIN", new BigDecimal("812.00"));
        demoPrices.put("ITC", new BigDecimal("448.00"));
    }

    public BigDecimal getCurrentPrice(String symbol) {
        String normalized = symbol.toUpperCase(Locale.ROOT);
        BigDecimal price = demoPrices.get(normalized);
        if (price != null) {
            return price;
        }

        ShareTransaction lastTransaction = shareTransactionRepository.findFirstBySymbolIgnoreCaseOrderByTransactionDateDescIdDesc(normalized);
        if (lastTransaction != null) {
            return lastTransaction.getPricePerShare();
        }

        return BigDecimal.ZERO;
    }
}
