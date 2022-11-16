package com.example.stockbot.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class StockInfo {
    String ticker;
    String figi;
    String name;
}
