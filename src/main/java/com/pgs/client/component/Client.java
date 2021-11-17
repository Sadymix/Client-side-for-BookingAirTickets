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

    private final UserClient userClient;
    private final ReservationClient reservationClient;
    private final PassengerClient passengerClient;
    private final AirportClient airportClient;
    private final FlightClient flightClient;
    private final WeatherClient weatherClient;

    private static UserDto userDto = UserDto.builder()
            .username("user0")
            .password("pass0")
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .enabled(true)
            .roles(List.of("USER"))
            .build();
    private static PassengerDto passengerDto = PassengerDto.builder()
            .firstName("Jim")
            .lastName("One")
            .email("JimOne@nose.org")
            .country("USA")
            .telephone("301242142")
            .build();
    private static PassengerDto passengerDto1 = PassengerDto.builder()
            .firstName("One")
            .lastName("One")
            .email("oneone@nose.org")
            .country("USA")
            .telephone("123141242")
            .build();
    private static ReservationDto reservationDto = ReservationDto.builder()
            .flightId(1L)
            .status(ReservationDto.ReservationStatus.IN_PROGRESS)
            .passengers(List.of(passengerDto))
            .userId(2L)
            .build();
    private static AirportDto airportDto = AirportDto.builder()
            .code("ASD")
            .name("Airport")
            .country("USA")
            .build();
    private static FlightDto flightDto = FlightDto.builder()
            .type(FlightDto.TypeOfFlight.ECONOMY)
            .departureDate(LocalDateTime.now())
            .arrivalDate(LocalDateTime.now())
            .departureAirportIataCode("TLG")
            .arrivalAirportIataCode("HAE")
            .build();

    @PostConstruct
    public void postConstruct() {
        userClientPostConstruct();
        reservationClientPostConstruct();
        passengerClientPostConstruct();
        airportClientPostConstruct();
        flightClientPostConstruct();
        weatherClient.getWeatherByCity("Warsaw");
    }

    private void userClientPostConstruct() {
        userClient.getSingleUser(1L);
        userClient.addUser(userDto);
        userClient.deactivateUser(1L);
        userClient.activateUser(1L);
        userClient.setUserRoles(1L, List.of("USER", "STAFF"));
    }

    private void reservationClientPostConstruct() {
        reservationClient.getReservationWithPassengersAndFlight(2L);
        reservationClient.getReservationsByFlight(1L);
        reservationClient.getReservationsForCurrentUser();
        reservationClient.getReservationsByUser(1L);
        reservationClient.addReservation(reservationDto);
        reservationClient.cancelReservation(2L);
        reservationClient.realizedReservation(2L);
        reservationClient.deleteReservation(1L);
    }

    private void passengerClientPostConstruct() {
        passengerClient.getPassengers();
        passengerClient.getSinglePassenger(2L);
        passengerClient.addPassenger(passengerDto);
        passengerClient.editPassenger(passengerDto1, 4L);
    }

    private void airportClientPostConstruct() {
        airportClient.getAirports();
        airportClient.getAirportById(1L);
        airportClient.getAirportByCode("YYC");
        airportClient.addAirport(airportDto);
        airportClient.editAirport(airportDto, 123L);
    }

    private void flightClientPostConstruct() {
        flightClient.getFlights();
        flightClient.getFlight(4L);
        flightClient.addFlight(flightDto);
        flightClient.editFlight(flightDto, 1L);
        flightClient.deleteFlight(3L);
    }

}
