package com.pgs.client.supplier;

import com.pgs.client.service.AuthenticationClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class AccessTokenSupplier {

    private final AuthenticationClient authenticationClient;
    private static String accessToken;
    private StopWatch stopWatch = StopWatch.createStarted();
    @Value("${security.token.ttl}")
    private int ttl;

    public synchronized String supplyToken() {
        if (stopWatch.getTime(TimeUnit.SECONDS) >= ttl || accessToken == null || !stopWatch.isStarted()) {
            accessToken = authenticationClient.getToken().getAccessToken();
            stopWatch.reset();
            stopWatch.start();
            return accessToken;
        }
        return accessToken;
    }
}
