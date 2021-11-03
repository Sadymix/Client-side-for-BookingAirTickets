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

    private static final Token TOKEN_DTO = Token.builder()
            .accessToken("asd")
            .tokenType("dsa")
            .refreshType("sda")
            .expiresIn(60)
            .scope("read")
            .build();

    @BeforeEach
    void testGetTokenSetUp() {
        setAuthenticationClientFields();
        when(authRestTemplate.postForObject(TOKEN_URL, setUpRequest(), Token.class))
                .thenReturn(TOKEN_DTO);
    }

    @Test
    void testGetToken() {
        var token = authenticationClient.getToken();
        verify(authRestTemplate).postForObject(TOKEN_URL, setUpRequest(), Token.class);
        assertEquals(TOKEN_DTO.getAccessToken(), token.getAccessToken());
        assertEquals(TOKEN_DTO.getTokenType(), token.getTokenType());
        assertEquals(TOKEN_DTO.getRefreshType(), token.getRefreshType());
        assertEquals(TOKEN_DTO.getExpiresIn(), token.getExpiresIn());
        assertEquals(TOKEN_DTO.getScope(), token.getScope());
    }

    private void setAuthenticationClientFields() {
        ReflectionTestUtils.setField(authenticationClient, "username", USERNAME);
        ReflectionTestUtils.setField(authenticationClient, "password", PASSWORD);
        ReflectionTestUtils.setField(authenticationClient, "grantType", GRANT_TYPE);
        ReflectionTestUtils.setField(authenticationClient, "tokenUrl", TOKEN_URL);
    }

    private HttpEntity<MultiValueMap<String, String>> setUpRequest() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", GRANT_TYPE);
        requestBody.add("username", USERNAME);
        requestBody.add("password", PASSWORD);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(requestBody, headers);
    }
}