package com.pgs.client.service;

import com.pgs.client.dto.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthenticationClient {

    private final RestTemplate authRestTemplate;

    @Value("${app.user.name}")
    private String username;
    @Value("${app.user.password}")
    private String password;
    @Value("${app.user.grantType}")
    private String grantType;
    @Value("${app.url.token}")
    private String tokenUrl;

    public Token getToken() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        setUpRequestBody(requestBody, grantType);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var request = new HttpEntity<>(requestBody, headers);
        return authRestTemplate.postForObject(
                tokenUrl,
                request,
                Token.class);
    }

    private void setUpRequestBody(MultiValueMap<String, String> requestBody, String grantType) {
        requestBody.add("grant_type", grantType);
        requestBody.add("username", username);
        requestBody.add("password", password);
    }
}