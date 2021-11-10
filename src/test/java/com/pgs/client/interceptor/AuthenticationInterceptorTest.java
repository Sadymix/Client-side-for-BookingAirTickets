package com.pgs.client.interceptor;

import com.pgs.client.supplier.AccessTokenSupplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
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
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private HttpRequest httpRequest;
    @Mock
    private ClientHttpRequestExecution clientHttpRequestExecution;
    @InjectMocks
    private AuthenticationInterceptor authenticationInterceptor;

    @Test
    void intercept() {
        byte[] body = new byte[20];
        new Random().nextBytes(body);
        when(accessTokenSupplier.supplyToken())
                .thenReturn("ASDASD");
        doNothing().when(httpRequest).getHeaders().setBearerAuth("1234");
        var intercept = authenticationInterceptor.intercept(
                httpRequest,
                body,
                clientHttpRequestExecution);
        verify(accessTokenSupplier).supplyToken();
        assertTrue(intercept.getHeaders().containsValue("ASDASD"));
    }
}