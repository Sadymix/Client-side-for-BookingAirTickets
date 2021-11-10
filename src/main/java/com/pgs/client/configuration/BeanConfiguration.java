package com.pgs.client.configuration;

import com.pgs.client.interceptor.AuthenticationInterceptor;
import com.pgs.client.interceptor.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final RestTemplateBuilder builder;
    @Value("${app.client.clientId}")
    private String clientId;
    @Value("${app.client.secret}")
    private String secret;

    @Bean
    public RestTemplate authRestTemplate() {
        return builder
                .basicAuthentication(clientId, secret)
                .build();
    }

    @Bean
    public RestTemplate restTemplate(LoggingInterceptor loggingInterceptor, AuthenticationInterceptor authenticationInterceptor) {
        return builder
                .additionalInterceptors(loggingInterceptor, authenticationInterceptor)
                .build();
    }
}