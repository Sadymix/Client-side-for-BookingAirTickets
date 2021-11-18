package com.pgs.client.supplier;

import com.pgs.client.authentication.AccessTokenSupplier;
import com.pgs.client.authentication.AuthenticationClient;
import com.pgs.client.dto.Token;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessTokenSupplierTest {

    private static final Token TOKEN1 = Token.builder()
            .accessToken("access1234")
            .refreshToken("refresh1234")
            .build();
    private static final Token TOKEN2 = Token.builder()
            .accessToken("access4321")
            .refreshToken("refresh4321")
            .build();
    @Mock
    private AuthenticationClient authenticationClient;
    @InjectMocks
    private AccessTokenSupplier accessTokenSupplier;
    @Mock
    private StopWatch stopWatch;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(accessTokenSupplier, "ttl", Duration.ofHours(1));
    }

    @Test
    void testSupplyToken() {
        when(authenticationClient.getToken())
                .thenReturn(TOKEN1);
        var accessTokenString = accessTokenSupplier.supplyToken();
        verify(authenticationClient).getToken();
        assertEquals(accessTokenString, TOKEN1.getAccessToken());

    }

    @SneakyThrows
    @Test
    void testNewTokenSupplyPastTTL() {
        ReflectionTestUtils.setField(accessTokenSupplier, "stopWatch", stopWatch);
        when(authenticationClient.getToken())
                .thenReturn(TOKEN1).thenReturn(TOKEN2);
        when(stopWatch.getTime(SECONDS)).thenReturn(0L).thenReturn(3600L);
        var accessTokenString1 = accessTokenSupplier.supplyToken();
        var accessTokenString2 = accessTokenSupplier.supplyToken();

        assertNotEquals(accessTokenString2, accessTokenString1);
    }

    @SneakyThrows
    @Test
    void testSupplyToken10Threads() {
        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        when(authenticationClient.getToken())
                .thenReturn(TOKEN1);
        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() ->
                    accessTokenSupplier.supplyToken());
        }
        service.shutdown();
        service.awaitTermination(5, SECONDS);
        verify(authenticationClient).getToken();
    }
}
