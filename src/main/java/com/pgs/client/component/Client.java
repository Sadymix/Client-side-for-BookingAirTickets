package com.pgs.client.component;

import com.pgs.client.dto.*;
import com.pgs.client.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@Profile("smoke-test")
@RequiredArgsConstructor
public class Client {

    private static final UserDto USER_DTO = UserDto.builder()
            .username("user0")
            .password("pass0")
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .enabled(true)
            .roles(List.of("USER"))
            .build();
    private static final PassengerDto PASSENGER_DTO = PassengerDto.builder()
            .firstName("Jim")
            .lastName("One")
            .email("JimOne@nose.org")
            .country("USA")
            .telephone("301242142")
            .build();
    private static final PassengerDto PASSENGER_DTO1 = PassengerDto.builder()
            .firstName("One")
            .lastName("One")
            .email("oneone@nose.org")
            .country("USA")
            .telephone("123141242")
            .build();
    private static final ReservationDto RESERVATION_DTO = ReservationDto.builder()
            .flightId(1L)
            .status(ReservationDto.ReservationStatus.IN_PROGRESS)
            .passengers(List.of(PASSENGER_DTO))
            .userId(2L)
            .build();
    private static final AirportDto AIRPORT_DTO = AirportDto.builder()
            .code("ASD")
            .name("Airport")
            .country("USA")
            .build();
    private static final FlightDto FLIGHT_DTO = FlightDto.builder()
            .type(FlightDto.TypeOfFlight.ECONOMY)
            .departureDate(LocalDateTime.now())
            .arrivalDate(LocalDateTime.now())
            .departureAirportIataCode("TLG")
            .arrivalAirportIataCode("HAE")
            .build();
    private final UserClient userClient;
    private final ReservationClient reservationClient;
    private final PassengerClient passengerClient;
    private final AirportClient airportClient;
    private final FlightClient flightClient;

    @PostConstruct
    public void postConstruct() {
        userClientDemo();
        reservationClientDemo();
        passengerClientDemo();
        airportClientDemo();
        flightClientDemo();
    }

    private void userClientDemo() {
        userClient.getSingleUser(1L);
        userClient.addUser(USER_DTO);
        userClient.deactivateUser(1L);
        userClient.activateUser(1L);
        userClient.setUserRoles(1L, List.of("USER", "STAFF"));
    }

    private void reservationClientDemo() {
        reservationClient.getReservationWithPassengersAndFlight(2L);
        reservationClient.getReservationsByFlight(1L);
        reservationClient.getReservationsForCurrentUser();
        reservationClient.getReservationsByUser(1L);
        reservationClient.addReservation(RESERVATION_DTO);
        reservationClient.cancelReservation(2L);
        reservationClient.realizedReservation(2L);
        reservationClient.deleteReservation(1L);
    }

    private void passengerClientDemo() {
        passengerClient.getPassengers();
        passengerClient.getSinglePassenger(2L);
        passengerClient.addPassenger(PASSENGER_DTO);
        passengerClient.editPassenger(PASSENGER_DTO1, 4L);
    }

    private void airportClientDemo() {
        airportClient.getAirports();
        airportClient.getAirportById(1L);
        airportClient.getAirportByCode("YYC");
        airportClient.addAirport(AIRPORT_DTO);
        airportClient.editAirport(AIRPORT_DTO, 123L);
    }

    private void flightClientDemo() {
        flightClient.getFlights();
        flightClient.getFlight(4L);
        flightClient.addFlight(FLIGHT_DTO);
        flightClient.editFlight(FLIGHT_DTO, 1L);
        flightClient.deleteFlight(3L);
    }

}
