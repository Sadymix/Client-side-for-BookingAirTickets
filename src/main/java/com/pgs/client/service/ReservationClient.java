package com.pgs.client.service;

import com.pgs.client.component.Client;
import com.pgs.client.dto.ReservationDto;
import com.pgs.client.dto.list.ReservationDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.reservations")
    private String apiReservationsUrl;
    private static HttpHeaders headers = getHeaders();

    public ReservationDto getReservationWithPassengersAndFlight(Long id) {
        var request = new HttpEntity<>(headers);
        return restTemplate.getForObject(
                apiReservationsUrl + "/" + id,
                ReservationDto.class,
                request);
    }

    public List<ReservationDto> getReservationsByFlight(Long id) {
        var request = new HttpEntity<>(headers);
        return restTemplate.getForObject(
                apiReservationsUrl + "/flights/" + id,
                ReservationDtoList.class,
                request).getReservations();
    }

    public List<ReservationDto> getReservationsForCurrentUser() {
        return restTemplate.getForObject(
                apiReservationsUrl + "/users",
                ReservationDtoList.class).getReservations();
    }

    public List<ReservationDto> getReservationsByUser(Long id) {
        var request = new HttpEntity<>(headers);
        return restTemplate.getForObject(
                apiReservationsUrl + "/users/" + id,
                ReservationDtoList.class,
                request).getReservations();
    }

    public ReservationDto addReservation(ReservationDto reservationDto) {
        var request = new HttpEntity<>(reservationDto, headers);
        return restTemplate.postForObject(
                apiReservationsUrl,
                request,
                ReservationDto.class);
    }

    public String deleteReservation(Long id) {
        restTemplate.delete(apiReservationsUrl + "/" + id);
        return "Success delete reservation with id: " + id;
    }

    public ReservationDto cancelReservation(Long id) {
        return reservationDtoChangeReservationStatus(apiReservationsUrl + "/" + id + "/canceled");
    }

    public ReservationDto realizedReservation(Long id) {
        return reservationDtoChangeReservationStatus(apiReservationsUrl + "/" + id + "/realized");
    }

    private ReservationDto reservationDtoChangeReservationStatus(String url) {
        var request = new HttpEntity<>(headers);
        return restTemplate.postForObject(url,
                request,
                ReservationDto.class);
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Client.TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
