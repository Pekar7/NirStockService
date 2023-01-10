package com.example.stockbot.controller;


import com.example.stockbot.model.PriceInfo;
import com.example.stockbot.model.Stock;
import com.example.stockbot.model.StockInfo;
import com.example.stockbot.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.invest.openapi.model.rest.Candles;

import java.io.File;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/stock")
public class StockController {
    private final StockService stockService;

    @Operation(summary = "Get Stock Information by Ticker", tags = "Ticker")
    @GetMapping("getStock/{ticker}")
    public Stock getStockByTicker(@PathVariable String ticker){
        return stockService.getStockByTicker(ticker);
    }

    @Operation(summary = "Get Candles Information interval 1 hour, for 1 day", tags = "Ticker")
    @GetMapping("getCandles/{ticker}")
    public Candles getInfoCandlesByFigi(@PathVariable String ticker) {
        return stockService.getCandles(ticker);
    }

    @Operation(summary = "Download CSV file with SMA Indecator", tags = "Indecators")
    @GetMapping("/files/SMA/{ticker}")
    @ResponseBody
    public ResponseEntity<Resource> getFileSMA(@PathVariable String ticker) {
        Resource file = stockService.loadAsResourceSMA(ticker);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @Operation(summary = "Download CSV file with EMA Indecator", tags = "Indecators")
    @GetMapping("/files/EMA/{ticker}")
    @ResponseBody
    public ResponseEntity<Resource> getFileEMA(@PathVariable String ticker) {
        Resource file = stockService.loadAsResourceEMA(ticker);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
