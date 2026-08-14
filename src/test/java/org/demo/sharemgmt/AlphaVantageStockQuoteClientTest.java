package org.demo.sharemgmt;

import java.math.BigDecimal;
import org.demo.sharemgmt.service.AlphaVantageStockQuoteClient;
import org.demo.sharemgmt.service.model.LiveStockQuote;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class AlphaVantageStockQuoteClientTest {

    @Test
    void parsesGlobalQuoteResponse() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();

        String body = "{\n"
            + "  \"Global Quote\": {\n"
            + "    \"01. symbol\": \"TCS\",\n"
            + "    \"05. price\": \"3925.0000\",\n"
            + "    \"08. previous close\": \"3880.0000\",\n"
            + "    \"09. change\": \"45.0000\",\n"
            + "    \"10. change percent\": \"1.16%\"\n"
            + "  }\n"
            + "}";

        server.expect(requestTo("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=TCS&apikey=test-key"))
            .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        AlphaVantageStockQuoteClient client = new AlphaVantageStockQuoteClient(
            restTemplate,
            "https://www.alphavantage.co/query",
            "test-key"
        );

        LiveStockQuote quote = client.fetchQuote("tcs").orElseThrow(IllegalStateException::new);

        assertEquals("TCS", quote.getSymbol());
        assertEquals(new BigDecimal("3925.0000"), quote.getCurrentPrice());
        assertEquals(new BigDecimal("3880.0000"), quote.getPreviousClose());
        assertEquals(new BigDecimal("45.0000"), quote.getChange());
        assertEquals(new BigDecimal("1.16"), quote.getChangePercent());
        assertTrue(quote.isLive());
        server.verify();
    }
}
