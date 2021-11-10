package com.pgs.client.supplier;

import com.pgs.client.dto.Token;
import com.pgs.client.service.AuthenticationClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenSupplier {

    private final AuthenticationClient authenticationClient;
    private static Token TOKEN;

    public synchronized String supplyToken() {
        var stopWatch = new StopWatch();
        TOKEN = authenticationClient.getToken();
        stopWatch.start();
        if(stopWatch.getTime()<TOKEN.getExpiresIn()) {
            return TOKEN.getAccessToken();
        }else {
            stopWatch.reset();
            return authenticationClient.getTokenWithRefreshToken(TOKEN).getAccessToken();
        }
    }
}
