package com.example.stockbot.service;



import com.example.stockbot.exception.StockNotFoundException;
import com.example.stockbot.model.*;
import com.example.stockbot.model.Currency;
import com.example.stockbot.service.indecators.EMAIndicator;
import com.example.stockbot.service.indecators.SMAIndicator;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.*;


import java.io.File;
import java.io.FileWriter;;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final OpenApi api;


    @Override
    public Stock getStockByTicker(String ticker) {
        var contextApi = api.getMarketContext().searchMarketInstrumentsByTicker(ticker);
        var listContext = contextApi.join().getInstruments();
        if (listContext.isEmpty()) {
            log.warn("List Context Stock By Ticker is Empty");
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
    public Candles getCandles(String ticker) {
        String figi = getStockByTicker(ticker).getFigi();
        var context = api.getMarketContext()
                .getMarketCandles(figi, OffsetDateTime.now().minusMonths(5), OffsetDateTime.now(), CandleResolution.DAY);
        var listContext = context.join().get().getCandles();


        BarSeries series = new BaseBarSeriesBuilder().build();
        List<String[]> data = new ArrayList<String[]>();

        for (int i = 0; i < listContext.size(); i++) {
            data.add(new String[]{String.valueOf(listContext.get(i).getC()), "Candle", listContext.get(i).getTime().format(DateTimeFormatter.ofPattern("dd:MM"))});
        }


        File file = new File("src/main/resources/data/AnalyseDataSet.csv");
        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            writer.writeAll(data);
            writer.close();

        } catch (Exception e) {
            log.warn("Candles dosen't exist " , e.getCause());
        }


        return context.join().get();
    }

    @Override
    public File getCandlesBySMA(String ticker) {
        String figi = getStockByTicker(ticker).getFigi();
        var context = api.getMarketContext()
                    .getMarketCandles(figi, OffsetDateTime.now().minusMonths(5), OffsetDateTime.now(), CandleResolution.DAY);
            var listContext = context.join().get().getCandles();

            BarSeries series = new BaseBarSeriesBuilder().build();

            for (int i = 0; i < listContext.size(); i++) {
                series.addBar(ZonedDateTime.from(listContext.get(i).getTime()), listContext.get(i).getO(),
                        listContext.get(i).getH(), listContext.get(i).getL(), listContext.get(i).getC(), listContext.get(i).getV());

            }

            ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
            SMAIndicator shortSma = new SMAIndicator(closePrice, 20); //период сглаживания
            List<String[]> dataSMA = new ArrayList<String[]>();
            double scale = Math.pow(10, 3);
            for (int i = 2; i < series.getBarCount(); i++) {
                dataSMA.add(new String[]{String.valueOf(Math.ceil(shortSma.getValue(i).doubleValue() * scale)/scale), "SMAIndecator", String.valueOf(ZonedDateTime.from(series.getBar(i).getBeginTime()))});
            }

            File fileSMA = new File("src/main/resources/data/AnalyseDataSetSMA.csv").getAbsoluteFile();
            try {
                FileWriter outputfileSMA = new FileWriter(fileSMA);
                CSVWriter writerSMA = new CSVWriter(outputfileSMA);
                writerSMA.writeAll(dataSMA);
                writerSMA.close();
            } catch (Exception e) {
                log.warn("Candles dosen't exist " , e.getCause());
        }
        return fileSMA;
    }


    @Override
    public Resource loadAsResourceSMA(String ticker) {

        File a = getCandlesBySMA(ticker);
        try {
            Path file = Paths.get("src/main/resources/data/AnalyseDataSetSMA.csv");
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }


    @Override
    public File getCandlesByEMA(String ticker) {
        String figi = getStockByTicker(ticker).getFigi();
        var context = api.getMarketContext()
                .getMarketCandles(figi, OffsetDateTime.now().minusMonths(5), OffsetDateTime.now(), CandleResolution.DAY);
        var listContext = context.join().get().getCandles();

        BarSeries series = new BaseBarSeriesBuilder().build();

        for (int i = 0; i < listContext.size(); i++) {
            series.addBar(ZonedDateTime.from(listContext.get(i).getTime()), listContext.get(i).getO(),
                    listContext.get(i).getH(), listContext.get(i).getL(), listContext.get(i).getC(), listContext.get(i).getV());

        }

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        EMAIndicator emaIndicator = new EMAIndicator(closePrice, 20);
        List<String[]> dataEMA = new ArrayList<String[]>();
        double scale = Math.pow(10, 3);
        for (int i = 2; i < series.getBarCount(); i++) {
            dataEMA.add(new String[]{String.valueOf(Math.ceil(emaIndicator.getValue(i).doubleValue() * scale)/scale), "EMAIndecator" , String.valueOf(ZonedDateTime.from(series.getBar(i).getBeginTime()))});
        }

        File fileEMA = new File("src/main/resources/data/AnalyseDataSetEMA.csv").getAbsoluteFile();
        try {
            FileWriter outputfileEMA = new FileWriter(fileEMA);
            CSVWriter writerEMA = new CSVWriter(outputfileEMA);
            writerEMA.writeAll(dataEMA);
            writerEMA.close();
        } catch (Exception e) {
            log.warn("Candles dosen't exist " , e.getCause());
        }
        return fileEMA;
    }

    @Override
    public Resource loadAsResourceEMA(String ticker) {
        File a = getCandlesByEMA(ticker);
        try {
            Path file = Paths.get("src/main/resources/data/AnalyseDataSetEMA.csv");
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}