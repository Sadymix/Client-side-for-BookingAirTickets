package com.pgs.client.supplier;

import com.pgs.client.dto.Token;
import com.pgs.client.service.AuthenticationClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessTokenSupplierTest {

    @Mock
    private AuthenticationClient authenticationClient;
    @InjectMocks
    private AccessTokenSupplier accessTokenSupplier;

    private static final Token TOKEN = Token.builder()
            .accessToken("access1234")
            .refreshToken("refresh1234")
            .build();

    @Test
    void testSupplyToken() {
        when(authenticationClient.getToken())
                .thenReturn(TOKEN);
        var accessTokenString = accessTokenSupplier.supplyToken();
        verify(authenticationClient).getToken();
        assertEquals(accessTokenString, TOKEN.getAccessToken());

    }
}