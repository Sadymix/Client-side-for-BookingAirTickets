package com.pgs.client.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private record LogRequestData(URI url, HttpMethod method, HttpHeaders requestHeaders, String requestBody) {
    }

    private record LogResponseData(HttpStatus statusCode, HttpHeaders headers, String body) {
    }

    @Override
    @SneakyThrows(IOException.class)
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) {

        var logRequestData = setUpLogRequestData(request, body);
        LogResponseData logResponseData = null;
        try {
            var response = execution.execute(request, body);
            logResponseData = setUpLogResponseData(response);
            return response;
        } finally {
            if (logResponseData == null) {
                logResponseData = new LogResponseData(null, null, null);
            }
            log.info("""
                                                        
                            REQUEST
                            URL: {}
                            Method: {}
                            Headers: {}
                            Body: {}
                            RESPONSE
                            Status: {}
                            Headers: {}
                            Body: {}
                            """,
                    logRequestData.url, logRequestData.method, logRequestData.requestHeaders, logRequestData.requestBody,
                    logResponseData.statusCode, logResponseData.headers, logResponseData.body);
        }
    }

    private LogRequestData setUpLogRequestData(HttpRequest request, byte[] body) {
        return new LogRequestData(request.getURI(), request.getMethod(), request.getHeaders(), new String(body));
    }

    private LogResponseData setUpLogResponseData(ClientHttpResponse response) throws IOException {
        return new LogResponseData(
                response.getStatusCode(),
                response.getHeaders(),
                StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
    }
}
