package com.pgs.client.service;

import com.pgs.client.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.reservations}")
    private String apiReservationsUrl;

    public ReservationDto getReservationWithPassengersAndFlight(Long id) {
        return restTemplate.getForObject(
                apiReservationsUrl + "/" + id,
                ReservationDto.class);
    }

    public List<ReservationDto> getReservationsByFlight(Long id) {
        return exchangeAsList(apiReservationsUrl + "/flights/" + id);
    }

    public List<ReservationDto> getReservationsForCurrentUser() {
        return exchangeAsList(
                apiReservationsUrl + "/users");
    }

    public List<ReservationDto> getReservationsByUser(Long id) {
        return exchangeAsList(apiReservationsUrl + "/users/" + id);
    }

    public ReservationDto addReservation(ReservationDto reservationDto) {
        var request = new HttpEntity<>(reservationDto);
        return restTemplate.postForObject(
                apiReservationsUrl,
                request,
                ReservationDto.class);
    }

    public ReservationDto cancelReservation(Long id) {
        return reservationDtoChangeReservationStatus(apiReservationsUrl + "/" + id + "/canceled");
    }

    public ReservationDto realizedReservation(Long id) {
        return reservationDtoChangeReservationStatus(apiReservationsUrl + "/" + id + "/realized");
    }

    public void deleteReservation(Long id) {
        restTemplate.delete(apiReservationsUrl + "/" + id);
    }

    private <T> List<T> exchangeAsList(String url) {
        var responseType = new ParameterizedTypeReference<List<T>>() {
        };
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType).getBody();
    }

    private ReservationDto reservationDtoChangeReservationStatus(String url) {
        return restTemplate.exchange(url,
                HttpMethod.PUT,
                null,
                ReservationDto.class).getBody();
    }
}
