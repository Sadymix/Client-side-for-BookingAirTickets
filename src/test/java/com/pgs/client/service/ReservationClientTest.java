package com.pgs.client.service;

import com.pgs.client.dto.PassengerDto;
import com.pgs.client.dto.ReservationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationClientTest {

    @Mock
    private ResponseEntity<ReservationDto> responseEntity;
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
    private static final ReservationDto RESERVATION_CANCELED = ReservationDto.builder()
            .status(ReservationDto.ReservationStatus.CANCELED)
            .build();
    private static final ReservationDto RESERVATION_REALIZED = ReservationDto.builder()
            .status(ReservationDto.ReservationStatus.REALIZED)
            .build();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(reservationClient, "apiReservationsUrl", URL);
    }

    @Test
    void testGetReservationWithPassengersAndFlight() {
        when(restTemplate.getForObject(eq(URL + "/12"), eq(ReservationDto.class)))
                .thenReturn(RESERVATION);
        var reservation = reservationClient.getReservationWithPassengersAndFlight(12L);
        verify(restTemplate).getForObject(anyString(), any(Class.class));
        assertThat(reservation).isNotNull();
        assertThat(reservation).isEqualTo(RESERVATION);
    }



    @Test
    void testAddReservation() {
        when(restTemplate.postForObject(eq(URL), any(HttpEntity.class), eq(ReservationDto.class)))
                .thenReturn(RESERVATION);
        var reservation = reservationClient.addReservation(RESERVATION);
        verify(restTemplate).postForObject(anyString(), any(HttpEntity.class), any(Class.class));
        assertThat(reservation).isEqualTo(RESERVATION);
    }

    @Test
    void testCancelReservation() {
        when(restTemplate.exchange(eq(URL + "/" + 1 + "/canceled"), eq(HttpMethod.PUT),
                any(HttpEntity.class), eq(ReservationDto.class)))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(RESERVATION_CANCELED);
        var reservation = reservationClient.cancelReservation(1L);
        assertThat(reservation.getStatus()).isEqualTo(RESERVATION_CANCELED.getStatus());
    }

    @Test
    void testRealizedReservation() {
        when(restTemplate.exchange(eq(URL + "/" + 1 + "/realized"), eq(HttpMethod.PUT),
                any(HttpEntity.class), eq(ReservationDto.class)))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(RESERVATION_REALIZED);
        var reservation = reservationClient.realizedReservation(1L);
        assertThat(reservation.getStatus()).isEqualTo(RESERVATION_REALIZED.getStatus());
    }

    @Test
    void testDeleteReservation() {
        reservationClient.deleteReservation(1L);
        verify(restTemplate).delete(URL + "/" + 1);
    }

    @Nested
    class getListOfReservationDtoTestCases {

        @Mock
        private ResponseEntity<List<ReservationDto>> responseEntityList;

        @BeforeEach
        void setUp() {
            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    nullable(HttpEntity.class),
                    any(ParameterizedTypeReference.class)))
                    .thenReturn(responseEntityList);
            when(responseEntityList.getBody()).thenReturn(List.of(RESERVATION));
        }

        @Test
        void testGetReservationsByFlight() {
            var reservations = reservationClient.getReservationsByFlight(1L);
            assertThat(reservations).isNotNull();
            assertThat(reservations).isEqualTo(List.of(RESERVATION));
        }

        @Test
        void testGetReservationsForCurrentUser() {
            var reservations = reservationClient.getReservationsForCurrentUser();
            assertThat(reservations).isNotNull();
            assertThat(reservations).isEqualTo(List.of(RESERVATION));
        }

        @Test
        void testGetReservationsByUser() {
            var reservations = reservationClient.getReservationsByUser(1L);
            assertThat(reservations).isNotNull();
            assertThat(reservations).isEqualTo(List.of(RESERVATION));
        }
    }
}
