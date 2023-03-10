package com.example.stockbot.service;

import com.example.stockbot.config.ApiConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ServiceBot extends TelegramLongPollingBot {
    final ApiConfig config;
    final StockService stockService;

    static final String HELP_TEXT = "Этот бот обрабатывает и выдает данные по акциям через Тикер.\nТикер — это краткое название финансового инструмента на бирже\n" +
            "Тикер можно найти тут: https://clck.ru/32ViCF\n\n";

    static final String TEXT_BIO = "Этот бот создал karen_ahper.\n\n" +
            "GitHub: Pekar7\nTelegram: @karen_ahper\nInstagram: https://instagram.com/karen_ahper?igshid=YmMyMTA2M2Y=\n\n"
            +"P.S у иеня есть твои данные :)";

    @Autowired
    public ServiceBot(ApiConfig config, StockService stockService) {
        this.config = config;
        this.stockService = stockService;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "Активируйте бота"));
        listofCommands.add(new BotCommand("/help", "Информация от бота"));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default:
                    getStockByTickerBot(chatId, messageText);
                    sendPNG(chatId, messageText);
                    break;
            }
        }

    }


    private void getStockByTickerBot(long chatId, String messageText) {
        var answer = stockService.getStockByTicker(messageText);
        if (answer==null) {
            sendMessage(chatId, "Извините акция с тикером '" + messageText + "' не найдена\nЕсли нужан помощь используйте команду /help");
        } else {
            String text = "Акция: " + answer.getName() + "\nЦена: " + answer.getPrice() + " " + answer.getCurrency()
                    + "\nПродается в количестве: " + answer.getLot() +" лота\nFIGI:" +answer.getFigi()+ "\nTICKER: " + answer.getTicker() +"\n\n"+
                    "Скачать CSV с данными:\n";
            //"1)Свечи за период: " + answerCandles  + "\n2)Индектор SMA: " + answerSMA + "\n3)Индектор EMA: " + answerEMA;
            sendMessage(chatId, text);
        }
    }

//    @SneakyThrows
//    private void sendDocument(long chatId, String messageText) {
//        stockService.getCandles(messageText);
//        SendDocument sendDocument = new SendDocument();
//        sendDocument.setChatId(chatId);
//        sendDocument.getDocument();
//        sendDocument.setDocument(new InputFile(new File("src/main/resources/data/AnalyseDataSet.csv"), "AnalyseDataSet.csv"));
//        execute(sendDocument);
//    }

    @SneakyThrows
    private void sendPNG(long chatId, String messageText) {
        stockService.getCandles(messageText);
        SendPhoto sendDocument = new SendPhoto();
        sendDocument.setChatId(chatId);
        sendDocument.setPhoto(new InputFile(new File("src/main/resources/data/Diagram.png"), "Diagram.png"));
        execute(sendDocument);
    }




    private void startCommandReceived(long chatId, String firstName) {
        String answer = "Привет, " + firstName + "! \nВас приветсвует виртуальный ассистент @MisisServiceBot. \nНаш бот позволяет получать информацию по ценным бумагам, используя Тикеры!"
                + "\nУкажите Тикер.\n\nНАПРИМЕР: TSLA или SBER\nНажмите /help чтобы найти список тикеров";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);

        try {
            execute(sendMessage);
        } catch (TelegramApiException ex) {
            log.error("Exception ", ex);
        }
    }
}