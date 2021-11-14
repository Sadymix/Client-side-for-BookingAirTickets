package com.pgs.client.interceptor;

import com.pgs.client.supplier.AccessTokenSupplier;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

    @Mock
    private AccessTokenSupplier accessTokenSupplier;
    @Mock
    private HttpRequest httpRequest;
    @Mock
    private ClientHttpRequestExecution clientHttpRequestExecution;
    @InjectMocks
    private AuthenticationInterceptor authenticationInterceptor;
    @Mock
    private ClientHttpResponse clientHttpResponse;

    @SneakyThrows
    @Test
    void intercept() {
        byte[] body = new byte[20];
        new Random().nextBytes(body);
        when(accessTokenSupplier.supplyToken())
                .thenReturn("ASDASD");
        when(httpRequest.getHeaders())
                .thenReturn(new HttpHeaders(getValueMap()));
        when(clientHttpRequestExecution.execute(httpRequest, body)).thenReturn(clientHttpResponse);
        var intercept = authenticationInterceptor.intercept(
                httpRequest,
                body,
                clientHttpRequestExecution);
        verify(accessTokenSupplier).supplyToken();
        verify(clientHttpRequestExecution).execute(httpRequest, body);
    }

    @Test
    void intercept_with10Threads() {
        byte[] body = new byte[20];
        new Random().nextBytes(body);
        when(accessTokenSupplier.supplyToken())
                .thenReturn("ASDASD");
        when(httpRequest.getHeaders()).thenReturn(new HttpHeaders(getValueMap()));
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            service.execute(() -> {
                authenticationInterceptor.intercept(
                        httpRequest,
                        body,
                        clientHttpRequestExecution);
                latch.countDown();
            });
        }
        verify(accessTokenSupplier).supplyToken();
    }

    private MultiValueMap<String, String> getValueMap() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("headers", "headers");
        headers.add("Authorization", "");
        return headers;
    }

}