package com.pgs.client.supplier;

import com.pgs.client.dto.Token;
import com.pgs.client.service.AuthenticationClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessTokenSupplierTest {

    @Mock
    private AuthenticationClient authenticationClient;
    @InjectMocks
    private AccessTokenSupplier accessTokenSupplier;

    private static final Token TOKEN1 = Token.builder()
            .accessToken("access1234")
            .refreshToken("refresh1234")
            .build();
    private static final Token TOKEN2 = Token.builder()
            .accessToken("access4321")
            .refreshToken("refresh4321")
            .build();

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
    void testInterceptNewTokenAssigmentPastTTL() {
        var duration1 = Duration.ofSeconds(0);
        var duration2 = Duration.ofSeconds(3600);
        String accessTokenString1 = "";
        String accessTokenString2 = "";
        when(authenticationClient.getToken())
                .thenReturn(TOKEN1).thenReturn(TOKEN2);
        if (duration1 == Duration.ofSeconds(0)) {
            ReflectionTestUtils.setField(accessTokenSupplier, "ttl", duration1);
            accessTokenString1 = accessTokenSupplier.supplyToken();
        }
        if (duration2 == Duration.ofSeconds(3600)) {
            ReflectionTestUtils.setField(accessTokenSupplier, "ttl", duration2);
            accessTokenString2 = accessTokenSupplier.supplyToken();
        }
        assertNotEquals(accessTokenString2, accessTokenString1);
    }

    @SneakyThrows
    @Test
    void TestIntercept10Threads() {
        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CompletableFuture<String> future = null;
        when(authenticationClient.getToken())
                .thenReturn(TOKEN1);
        ReflectionTestUtils.setField(accessTokenSupplier, "ttl", Duration.ofSeconds(3600));
        for (int i = 0; i < numberOfThreads; i++) {
            future = CompletableFuture.completedFuture(service.submit(() ->
                    accessTokenSupplier.supplyToken()).get());
            assertEquals(future.get(), TOKEN1.getAccessToken());
        }
        service.shutdown();
        service.awaitTermination(5, SECONDS);
        verify(authenticationClient).getToken();
    }
}