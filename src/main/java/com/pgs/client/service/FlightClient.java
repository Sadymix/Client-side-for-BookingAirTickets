package com.pgs.client.service;

import com.pgs.client.dto.FlightDto;
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
public class FlightClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.flights}")
    private String apiFlightsUrl;

    private static HttpHeaders headers = getHeaders();

    public List<FlightDto> getFlights() {
        var responseType = new ParameterizedTypeReference<List<FlightDto>>(){};
        return restTemplate.exchange(
                apiFlightsUrl,
                HttpMethod.GET,
                null,
                responseType).getBody();
    }

    public FlightDto getFlight(Long id) {
        return restTemplate.getForObject(
                apiFlightsUrl +"/"+id,
                FlightDto.class);
    }

    public FlightDto addFlight(FlightDto flightDto) {
        var request = new HttpEntity<>(flightDto, headers);
        return restTemplate.postForObject(apiFlightsUrl,
                request,
                FlightDto.class);
    }

    public FlightDto editFlight(FlightDto flightDto, Long id) {
        var request = new HttpEntity<>(flightDto, headers);
        return restTemplate.exchange(
                apiFlightsUrl +"/" +id,
                HttpMethod.PUT,
                request,
                FlightDto.class).getBody();
    }

    public void deleteFlight(Long id) {
        restTemplate.delete(apiFlightsUrl +"/" + id);
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
