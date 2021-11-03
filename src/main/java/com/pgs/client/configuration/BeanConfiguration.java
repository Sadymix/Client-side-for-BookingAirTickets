package com.pgs.client.configuration;

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
    @Value("${app.user.name}")
    private String username;
    @Value("${app.user.password}")
    private String password;


    @Bean
    public RestTemplate restTemplate() {
        return builder.basicAuthentication(username, password).build();
    }
}