package com.pgs.client.service;

import com.pgs.client.dto.FlightDto;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightClientTest {

    @Mock
    private ResponseEntity<List<FlightDto>> responseEntityList;
    @Mock
    private ResponseEntity<FlightDto> responseEntity;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private FlightClient flightClient;

    private static final String URL = "http://localhost:8080/api/flights";
    private static final FlightDto FLIGHT = FlightDto.builder()
            .type(FlightDto.TypeOfFlight.ECONOMY)
            .departureDate(LocalDateTime.now())
            .arrivalDate(LocalDateTime.now())
            .departureAirportIataCode("ASD")
            .arrivalAirportIataCode("DSA")
            .build();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(flightClient, "apiFlightsUrl", URL);
    }

    @Test
    void testGetFlights() {
        when(restTemplate.exchange(
                eq(URL),
                eq(HttpMethod.GET),
                nullable(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntityList);
        when(responseEntityList.getBody()).thenReturn(List.of(FLIGHT));
        var flights = flightClient.getFlights();
        assertThat(flights).isNotNull();
        assertThat(flights).isEqualTo(List.of(FLIGHT));
    }

    @Test
    void testGetFlight() {
        when(restTemplate.getForObject(
                eq(URL + "/1"),
                eq(FlightDto.class)))
                .thenReturn(FLIGHT);
        var flight = flightClient.getFlight(1L);
        assertThat(flight).isNotNull();
        assertThat(flight).isEqualTo(FLIGHT);
    }

    @Test
    void testAddFlight() {
        when(restTemplate.postForObject(
                eq(URL),
                any(HttpEntity.class),
                eq(FlightDto.class)))
                .thenReturn(FLIGHT);
        var flight = flightClient.addFlight(FLIGHT);
        assertThat(flight).isNotNull();
        assertThat(flight).isEqualTo(FLIGHT);
    }

    @Test
    void testEditFlight() {
        when(restTemplate.exchange(
                eq(URL + "/1"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(FlightDto.class)))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(FLIGHT);
        var flight = flightClient.editFlight(FLIGHT, 1L);
        assertThat(flight).isNotNull();
        assertThat(flight).isEqualTo(FLIGHT);
    }

    @Test
    void testDeleteFlight() {
        flightClient.deleteFlight(1L);
        verify(restTemplate).delete(eq(URL + "/1"));
    }
}
