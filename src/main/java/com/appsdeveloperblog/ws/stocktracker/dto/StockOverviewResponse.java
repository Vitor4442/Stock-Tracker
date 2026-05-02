package com.appsdeveloperblog.ws.stocktracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockOverviewResponse(
        @JsonProperty("Symbol") String symbol,
        @JsonProperty("Name") String name,
        @JsonProperty("Description") String description,
        @JsonProperty("sector") String industry,
        @JsonProperty("MarketCapitalization") String marketCap,
        @JsonProperty("PERatio") String peRatio,
        @JsonProperty("DividendYield") String dividendYield
) { }
