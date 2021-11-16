package com.pgs.client.service;

import com.pgs.client.component.Client;
import com.pgs.client.dto.PassengerDto;
import com.pgs.client.dto.ReservationDto;
import com.pgs.client.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationClientTest {

    @Captor
    private ArgumentCaptor<HttpEntity<UserDto>> requestCaptor;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ReservationClient reservationClient;

    private static final String URL = "http://localhost:8080/api/reservations";
    private static final PassengerDto PASSENGER = PassengerDto.builder()
            .firstName("John")
            .build();
    private static final ReservationDto RESERVATION = ReservationDto.builder()
            .flightId(23L)
            .status(ReservationDto.ReservationStatus.IN_PROGRESS)
            .passengers(List.of(PASSENGER))
            .userId(12L)
            .build();

    @BeforeEach
    void setUp (){
        ReflectionTestUtils.setField(reservationClient, "apiReservationsUrl", URL);
        Client.TOKEN = "qwerty";
    }

    @Test
    void getReservationWithPassengersAndFlight() {
        when(restTemplate.getForObject(eq(URL), eq(ReservationDto.class), any(HttpEntity.class)))
                .thenReturn(RESERVATION);

        reservationClient.getReservationWithPassengersAndFlight(1L);
        verify(restTemplate).getForObject(anyString(), any(Class.class), requestCaptor.capture());
        var request = requestCaptor.getValue();
        assertThat(request).isNotNull();
        Assertions.assertThat(request.getHeaders().getValuesAsList(HttpHeaders.AUTHORIZATION))
                .hasSize(1)
                .contains("Bearer " + Client.TOKEN);
        assertThat(request.getBody()).isEqualTo(RESERVATION);

    }

    @Test
    void getReservationsByFlight() {
    }

    @Test
    void getReservationsForCurrentUser() {
    }

    @Test
    void getReservationsByUser() {
    }

    @Test
    void addReservation() {
    }

    @Test
    void deleteReservation() {
    }

    @Test
    void cancelReservation() {
    }

    @Test
    void realizedReservation() {
    }
}