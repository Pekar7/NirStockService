package com.example.stockbot.service;

import com.example.stockbot.model.Stock;
import org.springframework.core.io.Resource;
import ru.tinkoff.invest.openapi.model.rest.Candle;
import ru.tinkoff.invest.openapi.model.rest.Candles;
import java.io.File;
import java.util.List;

public interface StockService {
    Stock getStockByTicker(String ticker);
    Candles getCandles(String ticker);
    Resource loadAsResource(String ticker);
    File getCandlesBySMA(String ticker, List<Candle> listContext);
    File getCandlesByEMA(String ticker, List<Candle> listContext);
    //    Resource loadAsResourceSMA(String ticker);

}
