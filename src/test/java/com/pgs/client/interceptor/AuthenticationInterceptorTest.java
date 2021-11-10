package com.pgs.client.interceptor;

import com.pgs.client.supplier.AccessTokenSupplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

    @Mock
    private AccessTokenSupplier accessTokenSupplier;
    @InjectMocks
    private AuthenticationInterceptor authenticationInterceptor;

    @Test
    void intercept() {
        byte[] body = new byte[20];
        new Random().nextBytes(body);
        when(accessTokenSupplier.supplyToken())
                .thenReturn("ASDASD");
        var intercept = authenticationInterceptor.intercept(mock(HttpRequest.class), body, mock(ClientHttpRequestExecution.class));
        verify(accessTokenSupplier).supplyToken();
        assertTrue(intercept.getHeaders().containsValue("ASDASD"));
    }
}