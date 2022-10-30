package com.example.stockbot.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor
public class Stock {
    String ticker;
    String figi;
    Integer lot;
    String name;
    String type;
    Currency currency;
    BigDecimal price;
    String source;
}
