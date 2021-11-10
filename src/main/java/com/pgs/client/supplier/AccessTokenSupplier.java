package com.pgs.client.supplier;

import com.pgs.client.service.AuthenticationClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenSupplier {

    private final AuthenticationClient authenticationClient;

    public synchronized String supplyToken() {
        var stopWatch = new StopWatch();
        var token = authenticationClient.getToken();
        stopWatch.start();
        if(stopWatch.getTime()<3600000) {
            return token.getAccessToken();
        }else {
            stopWatch.reset();
            return authenticationClient.getTokenWithRefreshToken(token).getAccessToken();
        }
    }
}
