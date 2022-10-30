package com.example.stockbot.ping;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Log4j2
@Getter
@Setter
public class Ping {
    @Value("${pingtask.url}")
    private String url;

    @Scheduled(fixedRateString = "${pingtask.period}")
    public void ping() {
        try {
            URL url = new URL(getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            log.info("Ping {} OK", url.getHost(), connection);
            connection.disconnect();
        } catch (IOException e) {
            log.error("Ping не работает");
            e.printStackTrace();
        }
    }
}
