package com.pgs.client.service;

import com.pgs.client.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.reservations}")
    private String apiReservationsUrl;
    private static HttpHeaders headers = getHeaders();

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
        var request = new HttpEntity<>(reservationDto, headers);
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

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
    private <T> List<T> exchangeAsList(String url) {
        var responseType = new ParameterizedTypeReference<List<T>>(){};
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType).getBody();
    }
    private ReservationDto reservationDtoChangeReservationStatus(String url) {
        var request = new HttpEntity<>(headers);
        return restTemplate.exchange(url,
                HttpMethod.PUT,
                request,
                ReservationDto.class).getBody();
    }
}
