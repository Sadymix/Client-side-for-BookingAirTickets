package com.pgs.client.supplier;

import com.pgs.client.service.AuthenticationClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class AccessTokenSupplier {

    private final AuthenticationClient authenticationClient;
    private String accessToken;
    private StopWatch stopWatch = StopWatch.createStarted();
    @Value("${security.token.ttl}")
    private Duration ttl;

    public synchronized String supplyToken() {
        if (isAccessTokenNeeded()) {
            accessToken = authenticationClient.getToken().getAccessToken();
            stopWatch.reset();
            stopWatch.start();
        }
        return accessToken;
    }

    private boolean isAccessTokenNeeded() {
        return stopWatch.getTime(TimeUnit.SECONDS) >= ttl.toSeconds()
                || accessToken == null
                || !stopWatch.isStarted();
    }
}
