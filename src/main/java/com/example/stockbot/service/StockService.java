package com.example.stockbot.service;



import com.example.stockbot.model.PriceInfo;
import com.example.stockbot.model.Stock;
import com.example.stockbot.model.StockInfo;
import ru.tinkoff.invest.openapi.model.rest.Candles;

import java.util.List;

public interface StockService {
    Stock getStockByTicker(String ticker);
    PriceInfo getPriceByFigi(String figi);
    Candles getCandles(String figi);
    List<StockInfo> getInfo();
    String getIdFromPortfolio();
}
