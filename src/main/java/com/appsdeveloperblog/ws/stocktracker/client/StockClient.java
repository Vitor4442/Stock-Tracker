package com.appsdeveloperblog.ws.stocktracker.client;

import com.appsdeveloperblog.ws.stocktracker.dto.AlphaVantageResponse;
import com.appsdeveloperblog.ws.stocktracker.dto.StockHistoryResponse;
import com.appsdeveloperblog.ws.stocktracker.dto.StockOverviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StockClient {

    private final WebClient webClient;

    @Value("${alpha.vantage.api.key}")
    private String apiKey;

    public Mono<StockHistoryResponse> getStockHistory(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function", "TIME_SERIES_DAILY")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(StockHistoryResponse.class) ;
    }

    public Mono<AlphaVantageResponse> getStockQuote(String symbol){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function", "GLOBAL_QUOTE")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build()
                )
                .retrieve()
                .bodyToMono(AlphaVantageResponse.class);
    }

    public Mono<StockOverviewResponse> getStockOverview(String symbol){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function", "OVERVIEW")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(StockOverviewResponse.class);
    }
}
