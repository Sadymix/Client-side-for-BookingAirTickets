package com.pgs.client.service;

import com.pgs.client.dto.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationClientTest {

    @Mock
    private RestTemplate authRestTemplate;
    @InjectMocks
    private AuthenticationClient authenticationClient;

    private static final String USERNAME = "user";
    private static final String PASSWORD = "user";
    private static final String GRANT_TYPE = "password";
    private static final String TOKEN_URL = "http://localhost:8080/oauth/token";

    private static final Token TOKEN = Token.builder()
            .accessToken("asd")
            .tokenType("dsa")
            .refreshToken("sda")
            .expiresIn(60)
            .scope("read")
            .build();

    @BeforeEach
    void testGetTokenSetUp() {
        ReflectionTestUtils.setField(authenticationClient, "username", USERNAME);
        ReflectionTestUtils.setField(authenticationClient, "password", PASSWORD);
        ReflectionTestUtils.setField(authenticationClient, "grantType", GRANT_TYPE);
        ReflectionTestUtils.setField(authenticationClient, "tokenUrl", TOKEN_URL);
        when(authRestTemplate.postForObject(TOKEN_URL, setUpRequest(GRANT_TYPE), Token.class))
                .thenReturn(TOKEN);
    }

    @Test
    void testGetToken() {
        when(authRestTemplate.postForObject(TOKEN_URL, setUpRequest(GRANT_TYPE), Token.class))
                .thenReturn(TOKEN);
        var token = authenticationClient.getToken();
        verify(authRestTemplate).postForObject(TOKEN_URL, setUpRequest(GRANT_TYPE), Token.class);
        assertEquals(TOKEN.getAccessToken(), token.getAccessToken());
        assertEquals(TOKEN.getTokenType(), token.getTokenType());
        assertEquals(TOKEN.getRefreshToken(), token.getRefreshToken());
        assertEquals(TOKEN.getExpiresIn(), token.getExpiresIn());
        assertEquals(TOKEN.getScope(), token.getScope());
    }

    private HttpEntity<MultiValueMap<String, String>> setUpRequest(String grantType) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("username", USERNAME);
        requestBody.add("password", PASSWORD);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(requestBody, headers);
    }
}