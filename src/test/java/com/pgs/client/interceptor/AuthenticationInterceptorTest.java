package com.pgs.client.interceptor;

import com.pgs.client.authentication.AuthenticationInterceptor;
import com.pgs.client.authentication.AccessTokenSupplier;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.mock.http.client.MockClientHttpRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

    @Mock
    private AccessTokenSupplier accessTokenSupplier;
    @Mock
    private ClientHttpRequestExecution clientHttpRequestExecution;
    @InjectMocks
    private AuthenticationInterceptor authenticationInterceptor;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        when(accessTokenSupplier.supplyToken())
                .thenReturn("ASDF");
    }

    @SneakyThrows
    @Test
    void testIntercept() {
        var request = new MockClientHttpRequest();
        authenticationInterceptor.intercept(request, null, clientHttpRequestExecution);
        verify(accessTokenSupplier).supplyToken();
        assertTrue(request.getHeaders().containsKey("Authorization"));
    }
}