package com.pgs.client.supplier;

import com.pgs.client.service.AuthenticationClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenSupplier {

    private final AuthenticationClient authenticationClient;
    private String accessToken;
    private final StopWatch stopWatch;


    public synchronized String supplyToken() {
        accessToken = authenticationClient.getToken().getAccessToken();
        if(stopWatch.getTime() == 0){
            stopWatch.start();
        }
        if(stopWatch.getTime()>=3600000 || accessToken == null) {
            stopWatch.reset();
            return authenticationClient.getToken().getAccessToken();
        }
        return accessToken;
    }
}
