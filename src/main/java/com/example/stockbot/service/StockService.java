package com.example.stockbot.service;

import com.example.stockbot.model.PriceInfo;
import com.example.stockbot.model.Stock;
import org.springframework.core.io.Resource;
import ru.tinkoff.invest.openapi.model.rest.Candles;
import java.io.File;

public interface StockService {
    Stock getStockByTicker(String ticker);
    Candles getCandles(String ticker);
    File getCandlesBySMA(String ticker);
    Resource loadAsResourceSMA(String ticker);
    File getCandlesByEMA(String ticker);
    Resource loadAsResourceEMA(String ticker);
}
