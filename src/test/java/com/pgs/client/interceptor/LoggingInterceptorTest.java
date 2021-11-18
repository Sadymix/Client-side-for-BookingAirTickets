package com.pgs.client.interceptor;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith({OutputCaptureExtension.class, MockitoExtension.class})
class LoggingInterceptorTest {

    private static byte[] BODY = new byte[20];
    @Mock
    private HttpRequest httpRequest;
    @Mock
    private ClientHttpRequestExecution clientHttpRequestExecution;
    @Mock
    private ClientHttpResponse clientHttpResponse;

    private LoggingInterceptor loggingInterceptor = new LoggingInterceptor();

    @SneakyThrows
    @BeforeEach
    void setUp() {
        new Random().nextBytes(BODY);
        when(httpRequest.getURI())
                .thenReturn(new URI("http:localhost:8080/1"));
        when(httpRequest.getMethod())
                .thenReturn(HttpMethod.GET);
        when(httpRequest.getHeaders())
                .thenReturn(new HttpHeaders(getValueMap()));
    }

    @SneakyThrows
    @Test
    void testIntercept(CapturedOutput capturedOutput) {
        String bodyString = StreamUtils.copyToString(new ByteArrayInputStream(BODY), StandardCharsets.UTF_8);
        when(clientHttpRequestExecution.execute(httpRequest, BODY))
                .thenReturn(clientHttpResponse);
        when(clientHttpResponse.getStatusCode())
                .thenReturn(HttpStatus.OK);
        when(clientHttpResponse.getHeaders())
                .thenReturn(new HttpHeaders(getValueMap()));
        when(clientHttpResponse.getBody())
                .thenReturn(new ByteArrayInputStream(BODY));
        loggingInterceptor.intercept(httpRequest, BODY, clientHttpRequestExecution);

        assertThat(capturedOutput.getOut()).contains("URL: http:localhost:8080/1");
        assertThat(capturedOutput.getOut()).contains("Method: GET");
        assertThat(capturedOutput.getOut()).contains("Headers: [headers:\"headers\"]");
        assertThat(capturedOutput.getOut()).contains("Status: 200 OK");
        assertThat(capturedOutput.getOut()).contains("Body: " + bodyString);
    }

    @SneakyThrows
    @Test
    void testIntercept_failedRequest(CapturedOutput capturedOutput) {

        String bodyString = StreamUtils.copyToString(new ByteArrayInputStream(BODY), StandardCharsets.UTF_8);
        when(clientHttpRequestExecution.execute(httpRequest, BODY))
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> loggingInterceptor.intercept(httpRequest, BODY, clientHttpRequestExecution));
        assertThat(capturedOutput.getOut()).contains("URL: http:localhost:8080/1");
        assertThat(capturedOutput.getOut()).contains("Method: GET");
        assertThat(capturedOutput.getOut()).contains("Headers: [headers:\"headers\"]");
        assertThat(capturedOutput.getOut()).contains("Body: " + bodyString);
    }

    private MultiValueMap<String, String> getValueMap() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("headers", "headers");
        return headers;
    }
}