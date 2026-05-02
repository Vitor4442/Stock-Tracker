package com.appsdeveloperblog.ws.stocktracker.dto;

import lombok.Builder;

@Builder
public record DailyStockResponse(
        String date,
        double open,
        double close,
        double high,
        double low,
        long volume
) {
}
