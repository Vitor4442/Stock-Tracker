package com.appsdeveloperblog.ws.stocktracker.controller;

import com.appsdeveloperblog.ws.stocktracker.dto.StockResponse;
import com.appsdeveloperblog.ws.stocktracker.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
