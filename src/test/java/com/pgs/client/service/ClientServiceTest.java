package com.pgs.client.service;

import com.pgs.client.dto.TokenDto;
import com.pgs.client.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private RestTemplate restTemplate;

    private static final TokenDto TOKEN_DTO = TokenDto.builder()
            .accessToken("asd")
            .tokenType("dsa")
            .refreshType("sda")
            .expiresIn(60)
            .scope("read")
            .build();

    @Test
    void getToken() {
        var request = new HttpEntity<>(new UserDto("admin", "admin", "password"));
        when(restTemplate.postForObject("http://localhost:8080/oauth/token", request, TokenDto.class)).thenReturn(TOKEN_DTO);
        var token = clientService.getToken();
        assertEquals(TOKEN_DTO.getAccessToken(), token.getAccessToken());
        assertEquals(TOKEN_DTO.getTokenType(), token.getTokenType());
        assertEquals(TOKEN_DTO.getRefreshType(), token.getRefreshType());
        assertEquals(TOKEN_DTO.getExpiresIn(), token.getExpiresIn());
        assertEquals(TOKEN_DTO.getScope(), token.getScope());
    }
}