package com.example.stockbot.model;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.tinkoff.invest.openapi.model.rest.TradeStatus;

import java.math.BigDecimal;

@AllArgsConstructor
@Value
public class PriceInfo {
    BigDecimal lastPrice;
    Integer depth;
    BigDecimal limitUp;
    BigDecimal limitDown;
    TradeStatus tradeStatus;
}

