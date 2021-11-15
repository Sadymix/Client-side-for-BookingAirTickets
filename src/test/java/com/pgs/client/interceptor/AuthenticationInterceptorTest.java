package com.pgs.client.interceptor;

import com.pgs.client.dto.Token;
import com.pgs.client.service.AuthenticationClient;
import com.pgs.client.supplier.AccessTokenSupplier;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

    @Spy
    private AuthenticationClient authenticationClient = new AuthenticationClient(mock(RestTemplate.class));
    @Spy
    private AccessTokenSupplier accessTokenSupplier = new AccessTokenSupplier(authenticationClient);
    @Mock
    private HttpRequest httpRequest;
    @Mock
    private ClientHttpRequestExecution clientHttpRequestExecution;
    @InjectMocks
    private AuthenticationInterceptor authenticationInterceptor;
    @Mock
    private ClientHttpResponse clientHttpResponse;

    private static byte[] BODY = new byte[20];

    private static final Token TOKEN = Token.builder()
            .accessToken("ASDASD")
            .build();

    @SneakyThrows
    @BeforeEach
    void setUp() {
        new Random().nextBytes(BODY);
        ReflectionTestUtils.setField(accessTokenSupplier, "ttl", 3600);
        when(authenticationClient.getToken()).thenReturn(TOKEN);
        when(accessTokenSupplier.supplyToken())
                .thenReturn("ASDASD");
        when(httpRequest.getHeaders())
                .thenReturn(new HttpHeaders(getValueMap()));
        when(clientHttpRequestExecution.execute(httpRequest, BODY)).thenReturn(clientHttpResponse);
    }

    @SneakyThrows
    @Test
    void intercept() {
        authenticationInterceptor.intercept(
                httpRequest,
                BODY,
                clientHttpRequestExecution);
        verify(clientHttpRequestExecution).execute(httpRequest, BODY);
        verify(accessTokenSupplier).supplyToken();
        verify(authenticationClient).getToken();
    }

    @SneakyThrows
    @Test
    void TestIntercept10Threads() {
        when(clientHttpResponse.getHeaders()).thenReturn(new HttpHeaders(getValueMap()));
        ExecutorService service = Executors.newFixedThreadPool(10);
        Future<ClientHttpResponse> future = null;
        for (int i = 0; i < 10; i++) {
            future = service.submit(() ->
                    authenticationInterceptor.intercept(httpRequest, BODY, clientHttpRequestExecution));
        }
        verify(authenticationClient).getToken();
        assertTrue(future.get().getHeaders().containsKey("Authorization"));
    }

    @SneakyThrows
    @Test
    void TestInterceptNewTokenAssigmentPastTTL() {
        when(clientHttpResponse.getHeaders()).thenReturn(new HttpHeaders(getValueMap()));
        var intercept = authenticationInterceptor.intercept(
                httpRequest,
                BODY,
                clientHttpRequestExecution);
        ReflectionTestUtils.setField(accessTokenSupplier, "ttl", 4000);
        var intercept1 = authenticationInterceptor.intercept(
                httpRequest,
                BODY,
                clientHttpRequestExecution);
        assertNotEquals(intercept.getHeaders().getValuesAsList("Authorization").get(0),intercept1.getHeaders().getValuesAsList("Authorization").get(0));
    }

    private MultiValueMap<String, String> getValueMap() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("headers", "headers");
        return headers;
    }

}