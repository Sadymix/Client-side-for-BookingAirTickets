package com.pgs.client.service;

import com.pgs.client.dto.TokenDto;
import com.pgs.client.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ClientService {

    public final RestTemplate restTemplate;

    @Value("${app.user.name}")
    private String username;
    @Value("${app.user.password}")
    private String password;
    @Value("${app.token.url}")
    private String tokenUrl;

    public TokenDto getToken() {
        var request = new HttpEntity<>(new UserDto(username, password, "password"));
        return restTemplate.postForObject(tokenUrl, request, TokenDto.class);
    }
}