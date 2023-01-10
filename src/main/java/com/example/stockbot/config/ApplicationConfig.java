package com.example.stockbot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApi;


@Configuration
@EnableConfigurationProperties(ApiConfig.class)
@RequiredArgsConstructor
public class ApplicationConfig {
    private final ApiConfig apiConfig;

    @Bean
    public OpenApi api() {
        String ssoToken = "t.PCNnkvLT7uc3-NNn4OBApy_CYQfPMu1rnMrzSewQQ-D9--yfvXnaCdrzxOIzUcY17nRo8oaYMEy2o6Ed_DjpJw";
        return new OkHttpOpenApi(ssoToken, apiConfig.getIsSandBoxMode());
    }
}
