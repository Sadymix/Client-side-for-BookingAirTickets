package com.pgs.client.component;

import com.pgs.client.service.AuthenticationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class Client {

    private final AuthenticationClient clientService;

    @PostConstruct
    public void postConstruct() {
        var token = clientService.getToken();
        log.info("Token: {}", token);
    }
}