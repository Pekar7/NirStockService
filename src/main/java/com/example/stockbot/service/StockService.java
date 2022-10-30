package com.example.stockbot.service;



import com.example.stockbot.model.PriceInfo;
import com.example.stockbot.model.Stock;
import ru.tinkoff.invest.openapi.model.rest.Candles;

public interface StockService {
    Stock getStockByTicker(String ticker);
    PriceInfo getPriceByFigi(String figi);
    Candles getCandles(String figi);
}
