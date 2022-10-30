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
        String ssoToken = "t.XpsFA8KRlLLEv4Mx_GP1XnbJuBfOb4r1iCM2fl5CLIucKFt171l35X9nFR1iiIDrT98o_bWFD9IrCgkXdhOgxg";
        return new OkHttpOpenApi(ssoToken, apiConfig.getIsSandBoxMode());
    }
}
