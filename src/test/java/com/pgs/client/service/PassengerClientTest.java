package com.pgs.client.service;

import com.pgs.client.dto.PassengerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PassengerClientTest {


    @Mock
    private ResponseEntity<List<PassengerDto>> responseEntityList;
    @Mock
    private ResponseEntity<PassengerDto> responseEntity;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private PassengerClient passengerClient;

    private static final PassengerDto PASSENGER = PassengerDto.builder()
            .firstName("Wolfgang")
            .lastName("Mozart")
            .email("wolfgang.mozart@gmail.com")
            .country("Austria")
            .telephone("123123123")
            .build();
    
    private static final String URL = "http://localhost:8080/api/passengers";
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(passengerClient, "apiPassengersUrl", URL);
    }
    
    @Test
    void testGetPassengers() {
        when(restTemplate.exchange(eq(URL), eq(HttpMethod.GET),nullable(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntityList);
        when(responseEntityList.getBody()).thenReturn(List.of(PASSENGER));
        var passengers = passengerClient.getPassengers();
        assertThat(passengers).isNotNull();
        assertThat(passengers).isEqualTo(List.of(PASSENGER));
    }

    @Test
    void testGetSinglePassenger() {
        when(restTemplate.getForObject(
                eq(URL + "/" + 1),
                eq(PassengerDto.class)))
                .thenReturn(PASSENGER);
        var passenger = passengerClient.getSinglePassenger(1L);
        assertThat(passenger).isNotNull();
        assertThat(passenger).isEqualTo(PASSENGER);
    }

    @Test
    void testAddPassenger() {
        when(restTemplate.postForObject(
                eq(URL),
                any(HttpEntity.class),
                eq(PassengerDto.class)))
                .thenReturn(PASSENGER);
        var passenger = passengerClient.addPassenger(PASSENGER);
        assertThat(passenger).isNotNull();
        assertThat(passenger).isEqualTo(PASSENGER);
    }

    @Test
    void testEditPassenger() {
        when(restTemplate.exchange(
                eq(URL+"/"+1),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(PassengerDto.class)))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(PASSENGER);
        var passenger = passengerClient.editPassenger(PASSENGER, 1L);
        assertThat(passenger).isNotNull();
        assertThat(passenger).isEqualTo(PASSENGER);
    }
}
