package com.pgs.client.component;

import com.pgs.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class Client {

    private final ClientService clientService;

    @PostConstruct
    private void postConstruct() {
        clientService.getToken();
    }
}