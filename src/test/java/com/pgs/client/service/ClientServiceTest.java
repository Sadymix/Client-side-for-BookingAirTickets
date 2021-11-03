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
            .access_token("asd")
            .token_type("dsa")
            .refresh_type("sda")
            .expires_in(60)
            .scope("read")
            .build();

    @Test
    void getToken() {
        var request = new HttpEntity<>(new UserDto("admin", "admin", "password"));
        when(restTemplate.postForObject("http://localhost:8080/oauth/token", request, TokenDto.class)).thenReturn(TOKEN_DTO);
        var token = clientService.getToken();
        assertEquals(TOKEN_DTO.getAccess_token(), token.getAccess_token());
        assertEquals(TOKEN_DTO.getToken_type(), token.getToken_type());
        assertEquals(TOKEN_DTO.getRefresh_type(), token.getRefresh_type());
        assertEquals(TOKEN_DTO.getExpires_in(), token.getExpires_in());
        assertEquals(TOKEN_DTO.getScope(), token.getScope());
    }
}