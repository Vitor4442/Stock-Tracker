package com.appsdeveloperblog.ws.stocktracker.dto;

import lombok.Builder;

@Builder
public record StockResponse(String symbol, double price, String lastUpdated) {
}
