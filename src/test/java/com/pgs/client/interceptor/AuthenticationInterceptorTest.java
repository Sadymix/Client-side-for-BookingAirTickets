package com.pgs.client.interceptor;

import com.pgs.client.supplier.AccessTokenSupplier;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    private MockClientHttpRequest mockClientHttpRequest;
    private static byte[] BODY = null;

    private static final String TOKEN = "ASDF";

    @SneakyThrows
    @BeforeEach
    void setUp() {
        when(accessTokenSupplier.supplyToken())
                .thenReturn(TOKEN);
        when(mockClientHttpRequest.getHeaders())
                .thenReturn(new HttpHeaders(getValueMap()));
    }

    @SneakyThrows
    @Test
    void testIntercept() {
       var response=  authenticationInterceptor.intercept(
                mockClientHttpRequest,
                BODY,
                clientHttpRequestExecution);
        verify(clientHttpRequestExecution).execute(mockClientHttpRequest, BODY);
        verify(accessTokenSupplier).supplyToken();
        assertTrue(response.getHeaders().containsKey("Authorization"));
    }

    private MultiValueMap<String, String> getValueMap() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("headers", "headers");
        headers.add("Authorization", "ASDASD");
        return headers;
    }
}