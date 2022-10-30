package com.example.stockbot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties(prefix = "api")
@PropertySource("application.yml")
public class ApiConfig {
    private Boolean isSandBoxMode;

    @Value("${bot.nameBot}")
    String botName;

    @Value("${bot.tokenBot}")
    String token;
}
