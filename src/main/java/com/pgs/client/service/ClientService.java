package com.pgs.client.service;

import com.pgs.client.dto.TokenDto;
import com.pgs.client.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ClientService {

    public final RestTemplate restTemplate;

    private static String tokenUrl = "http://localhost:8080/oauth/token";

    public TokenDto getToken() {
        var request = new HttpEntity<>(new UserDto("admin", "admin", "password"));
        return restTemplate.postForObject(tokenUrl, request, TokenDto.class);
    }

}
