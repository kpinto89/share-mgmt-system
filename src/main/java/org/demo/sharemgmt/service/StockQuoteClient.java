package org.demo.sharemgmt.service;

import java.util.Optional;
import org.demo.sharemgmt.service.model.LiveStockQuote;

public interface StockQuoteClient {

    Optional<LiveStockQuote> fetchQuote(String symbol);
}
