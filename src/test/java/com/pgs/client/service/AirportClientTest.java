package com.pgs.client.service;

import com.pgs.client.dto.AirportDto;
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
class AirportClientTest {

    private static final String URL = "http://localhost:8080/api/airports";
    private static final AirportDto AIRPORT = AirportDto.builder()
            .code("ASD")
            .name("Airport")
            .country("US")
            .build();
    @Mock
    private ResponseEntity<AirportDto> responseEntity;
    @Mock
    private ResponseEntity<List<AirportDto>> responseEntityList;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private AirportClient airportClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(airportClient, "apiAirportsUrl", URL);
    }

    @Test
    void testGetAirports() {
        when(restTemplate.exchange(
                eq(URL),
                eq(HttpMethod.GET),
                nullable(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntityList);
        when(responseEntityList.getBody()).thenReturn(List.of(AIRPORT));
        var airports = airportClient.getAirports();
        assertThat(airports).isNotNull();
        assertThat(airports).isEqualTo(List.of(AIRPORT));
    }

    @Test
    void testGetAirportById() {
        when(restTemplate.getForObject(
                eq(URL + "/1"),
                eq(AirportDto.class)))
                .thenReturn(AIRPORT);
        var airport = airportClient.getAirportById(1L);
        assertThat(airport).isNotNull();
        assertThat(airport).isEqualTo(AIRPORT);
    }

    @Test
    void testGetAirportByCode() {
        when(restTemplate.getForObject(
                eq(URL + "?code=ASD"),
                eq(AirportDto.class)))
                .thenReturn(AIRPORT);
        var airport = airportClient.getAirportByCode("ASD");
        assertThat(airport).isNotNull();
        assertThat(airport).isEqualTo(AIRPORT);
    }

    @Test
    void testAddAirport() {
        when(restTemplate.postForObject(
                eq(URL),
                any(HttpEntity.class),
                eq(AirportDto.class)))
                .thenReturn(AIRPORT);
        var airport = airportClient.addAirport(AIRPORT);
        assertThat(airport).isNotNull();
        assertThat(airport).isEqualTo(AIRPORT);
    }

    @Test
    void testEditAirport() {
        when(restTemplate.exchange(
                eq(URL + "/1"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(AirportDto.class)))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(AIRPORT);
        var airport = airportClient.editAirport(AIRPORT, 1L);
        assertThat(airport).isNotNull();
        assertThat(airport).isEqualTo(AIRPORT);
    }
}
