package com.appsdeveloperblog.ws.stocktracker.controller;

import com.appsdeveloperblog.ws.stocktracker.dto.DailyStockResponse;
import com.appsdeveloperblog.ws.stocktracker.dto.FavoriteStockRequest;
import com.appsdeveloperblog.ws.stocktracker.dto.StockOverviewResponse;
import com.appsdeveloperblog.ws.stocktracker.dto.StockResponse;
import com.appsdeveloperblog.ws.stocktracker.entity.FavoriteStock;
import com.appsdeveloperblog.ws.stocktracker.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/{stockSymbol}")
    public Mono<StockResponse> getStock(@PathVariable String stockSymbol){
        return stockService.getStockForSymbol(stockSymbol.toUpperCase());
    }

    @GetMapping("/{stockSymbol}/overview")
    public Mono<StockOverviewResponse> getStockOverview(@PathVariable String stockSymbol){
        return stockService.getStockOverviewForSymbol(stockSymbol.toUpperCase());
    }

    @GetMapping("/{symbol}/history")
    public Flux<DailyStockResponse> getStockHistory(@PathVariable String symbol, @RequestParam(defaultValue = "30") int days){
        return stockService.getHistory(symbol.toUpperCase(), days);
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteStock> saveFavoriteStock(@RequestBody FavoriteStockRequest request) {
        final FavoriteStock saved = stockService.addFavorite(request.getSymbol());
        return ResponseEntity.ok(saved);
    }
}
