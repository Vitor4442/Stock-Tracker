package com.appsdeveloperblog.ws.stocktracker.service;

import com.appsdeveloperblog.ws.stocktracker.client.StockClient;
import com.appsdeveloperblog.ws.stocktracker.dto.DailyStockResponse;
import com.appsdeveloperblog.ws.stocktracker.dto.StockHistoryResponse;
import com.appsdeveloperblog.ws.stocktracker.dto.StockOverviewResponse;
import com.appsdeveloperblog.ws.stocktracker.dto.StockResponse;
import com.appsdeveloperblog.ws.stocktracker.entity.FavoriteStock;
import com.appsdeveloperblog.ws.stocktracker.exception.FavoriteAlreadyExistsException;
import com.appsdeveloperblog.ws.stocktracker.repository.FavoriteStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StockService {

    private final StockClient stockClient;
    private final FavoriteStockRepository favoriteStockRepository;

    public Mono<StockResponse> getStockForSymbol(final String stockSymbol) {
        return stockClient.getStockQuote(stockSymbol)
                .map(response -> StockResponse.builder()
                        .symbol(response.globalQuote().symbol())
                        .price(Double.parseDouble(response.globalQuote().price()))
                        .lastUpdated(response.globalQuote().lastTradingDay())
                        .build());
    }

    public Mono<StockOverviewResponse> getStockOverviewForSymbol(final String symbol){
        return stockClient.getStockOverview(symbol);
    }

    public Flux<DailyStockResponse> getHistory(String symbol, int days) {
        return stockClient.getStockHistory(symbol)
                .flatMapMany(response -> Flux.fromIterable(response.timeSeries().entrySet()))
                .sort((entry1, entry2) -> entry2.getKey().compareTo(entry1.getKey()))
                .take(days)
                .map(entry -> {
                    String date = entry.getKey();
                    StockHistoryResponse.DailyPrice price = entry.getValue();

                    return DailyStockResponse.builder()
                            .date(date)
                            .open(Double.parseDouble(price.open()))
                            .close(Double.parseDouble(price.close()))
                            .high(Double.parseDouble(price.high()))
                            .low(Double.parseDouble(price.low()))
                            .volume(Long.parseLong(price.volume()))
                            .build();
                });
    }

    @Transactional
    public FavoriteStock addFavorite(final String symbol){
        if(favoriteStockRepository.existsBySymbol(symbol)){
            throw new FavoriteAlreadyExistsException(symbol);
        }

        FavoriteStock favorite = FavoriteStock.builder()
                .symbol(symbol)
                .build();

        return favoriteStockRepository.save(favorite);
    }

}