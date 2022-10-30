package com.example.stockbot.controller;


import com.example.stockbot.model.PriceInfo;
import com.example.stockbot.model.Stock;
import com.example.stockbot.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.invest.openapi.model.rest.Candles;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;

    @Operation(summary = "Get Stock Information by TICKER", tags = "TICKER")
    @GetMapping("getStockByTICKER/{ticker}")
    public Stock getStockByTicker(String ticker){
        return stockService.getStockByTicker(ticker);
    }

    @Operation(summary = "Get Price Information by FIGI", tags = "FIGI")
    @GetMapping("getPriceInfoByFIGI/{figi}")
    public PriceInfo getPriceByFigi(@PathVariable String figi) {
        return stockService.getPriceByFigi(figi);
    }

    @Operation(summary = "Get Candles Information interval 1 hour, for 1 day", tags = "FIGI")
    @GetMapping("getCandleInfoByFIGI/{figi}")
    public Candles getInfoCandlesByFigi(@PathVariable String figi) {
        return stockService.getCandles(figi);
    }

}
