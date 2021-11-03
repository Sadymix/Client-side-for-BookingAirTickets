package com.pgs.client.service;

import com.pgs.client.dto.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationClientTest {

    @InjectMocks
    private AuthenticationClient authenticationClient;

    @Mock
    private RestTemplate authRestTemplate;

    @Value("${app.user.name}")
    private String username;
    @Value("${app.user.password}")
    private String password;
    @Value("${app.user.grantType}")
    private String grantType;
    @Value("${app.token.url}")
    private String tokenUrl;

    private static final Token TOKEN_DTO = Token.builder()
            .accessToken("asd")
            .tokenType("dsa")
            .refreshType("sda")
            .expiresIn(60)
            .scope("read")
            .build();

    @BeforeEach
    void testGetTokenSetUp() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("username", username);
        requestBody.add("password", password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var request = new HttpEntity<>(requestBody, headers);
        when(authRestTemplate.postForObject(tokenUrl, request, Token.class))
                .thenReturn(TOKEN_DTO);
    }

    @Test
    void testGetToken() {
        var token = authenticationClient.getToken();
        assertEquals(TOKEN_DTO.getAccessToken(), token.getAccessToken());
        assertEquals(TOKEN_DTO.getTokenType(), token.getTokenType());
        assertEquals(TOKEN_DTO.getRefreshType(), token.getRefreshType());
        assertEquals(TOKEN_DTO.getExpiresIn(), token.getExpiresIn());
        assertEquals(TOKEN_DTO.getScope(), token.getScope());
    }
}