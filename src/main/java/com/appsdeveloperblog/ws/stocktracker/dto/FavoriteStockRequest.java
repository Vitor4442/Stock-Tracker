package com.appsdeveloperblog.ws.stocktracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteStockRequest {
    private String symbol;
}
