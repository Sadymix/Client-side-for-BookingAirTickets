package com.pgs.client.interceptor;

import com.pgs.client.supplier.AccessTokenSupplier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements ClientHttpRequestInterceptor {

    private final AccessTokenSupplier accessTokenSupplier;

    @Override
    @SneakyThrows
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution){
        request.getHeaders().setBearerAuth(accessTokenSupplier.supplyToken());
        return execution.execute(request, body);
    }
}