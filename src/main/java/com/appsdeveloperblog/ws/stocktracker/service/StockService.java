package com.appsdeveloperblog.ws.stocktracker.service;

import com.appsdeveloperblog.ws.stocktracker.client.StockClient;
import com.appsdeveloperblog.ws.stocktracker.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockClient stockClient;

    public Mono<StockResponse> getStockForSymbol(final String stockSymbol) {
        return stockClient.getStockQuote(stockSymbol)
                .map(response -> StockResponse.builder()
                        .symbol(response.globalQuote().symbol())
                        .price(Double.parseDouble(response.globalQuote().price()))
                        .lastUpdated(response.globalQuote().lastTradingDay())
                        .build());
    }
}