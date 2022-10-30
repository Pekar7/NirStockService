package com.example.stockbot.service;



import com.example.stockbot.exception.StockNotFoundException;
import com.example.stockbot.model.Currency;
import com.example.stockbot.model.PriceInfo;
import com.example.stockbot.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.CandleResolution;
import ru.tinkoff.invest.openapi.model.rest.Candles;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {
    private final OpenApi api;


    @Override
    public Stock getStockByTicker(String ticker) {
        var contextApi = api.getMarketContext().searchMarketInstrumentsByTicker(ticker);
        var listContext = contextApi.join().getInstruments();
        if (listContext.isEmpty()){
            return null;
        }
        var item = listContext.get(0);
        String figi = listContext.get(0).getFigi();

        if (listContext.isEmpty()) {
            throw new StockNotFoundException(String.format("Stock %S not found.", ticker));
        }


        var apCon = api.getMarketContext().getMarketOrderbook(figi, 0);

        return new Stock(
                item.getTicker(),
                item.getFigi(),
                item.getLot(),
                item.getName(),
                item.getType().getValue(),
                Currency.valueOf(item.getCurrency().getValue()),
                apCon.join().get().getLastPrice(),
                "TINKOFF");
    }


    @Override
    public PriceInfo getPriceByFigi(String figi) {
        var context = api.getMarketContext().getMarketOrderbook(figi, 0);
        var list = context.join().get();
        return new PriceInfo(
                list.getLastPrice(),
                list.getDepth(),
                list.getLimitUp(),
                list.getLimitDown(),
                list.getTradeStatus());
    }

    @Override
    public Candles getCandles(String figi) {
        var context = api.getMarketContext()
                .getMarketCandles(figi, OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), CandleResolution.HOUR);
        return context.join().get();
    }

}