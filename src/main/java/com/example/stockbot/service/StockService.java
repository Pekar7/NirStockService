package com.example.stockbot.service;



import com.example.stockbot.model.PriceInfo;
import com.example.stockbot.model.Stock;
import com.example.stockbot.model.StockInfo;
import org.springframework.core.io.Resource;
import ru.tinkoff.invest.openapi.model.rest.Candles;

import java.io.File;
import java.util.List;

public interface StockService {
    Stock getStockByTicker(String ticker);
    PriceInfo getPriceByFigi(String figi);
    Candles getCandles(String figi);
    File getCandlesBySMA(String figi);
    Resource loadAsResourceSMA(String figi);
    File getCandlesByEMA(String figi);
    Resource loadAsResourceEMA(String figi);
}
