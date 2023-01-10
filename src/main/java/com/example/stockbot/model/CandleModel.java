package com.example.stockbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Value
@AllArgsConstructor
@Data
public class CandleModel {
    OffsetDateTime time;
    BigDecimal open;
    BigDecimal high;
    BigDecimal low;
    BigDecimal close;
}
