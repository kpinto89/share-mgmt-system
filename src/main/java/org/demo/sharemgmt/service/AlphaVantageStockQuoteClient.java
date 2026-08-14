package org.demo.sharemgmt.service;

import java.math.BigDecimal;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Optional;
import org.demo.sharemgmt.service.model.LiveStockQuote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class AlphaVantageStockQuoteClient implements StockQuoteClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;

    public AlphaVantageStockQuoteClient(
        RestTemplate restTemplate,
        @Value("${stock.market.alpha-vantage.base-url}") String baseUrl,
        @Value("${stock.market.alpha-vantage.api-key:}") String apiKey
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Override
    public Optional<LiveStockQuote> fetchQuote(String symbol) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return Optional.empty();
        }

        String normalized = symbol.toUpperCase(Locale.ROOT);
        URI uri = URI.create(baseUrl + "?function=GLOBAL_QUOTE&symbol=" + normalized + "&apikey=" + apiKey.trim());
        try {
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(uri, JsonNode.class);
            JsonNode quote = response.getBody() == null ? null : response.getBody().path("Global Quote");
            if (quote == null || quote.isMissingNode() || quote.isEmpty()) {
                return Optional.empty();
            }

            BigDecimal currentPrice = decimalValue(quote, "05. price");
            BigDecimal previousClose = decimalValue(quote, "08. previous close");
            BigDecimal change = decimalValue(quote, "09. change");
            BigDecimal changePercent = percentageValue(quote.path("10. change percent").asText(""));
            if (currentPrice == null || previousClose == null || change == null || changePercent == null) {
                return Optional.empty();
            }

            return Optional.of(
                LiveStockQuote.live(
                    normalized,
                    currentPrice,
                    previousClose,
                    change,
                    changePercent,
                    OffsetDateTime.now()
                )
            );
        } catch (RestClientException ex) {
            return Optional.empty();
        }
    }

    private BigDecimal decimalValue(JsonNode quote, String fieldName) {
        String value = quote.path(fieldName).asText("");
        if (value.trim().isEmpty()) {
            return null;
        }
        return new BigDecimal(value);
    }

    private BigDecimal percentageValue(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            return null;
        }
        return new BigDecimal(rawValue.replace("%", "").trim());
    }
}
