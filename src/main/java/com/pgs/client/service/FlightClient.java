package com.pgs.client.service;

import com.pgs.client.component.Client;
import com.pgs.client.dto.FlightDto;
import com.pgs.client.dto.list.FlightDtoList;
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
public class FlightClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.flights}")
    private String apiUsersUrl;

    private static HttpHeaders headers = getHeaders();

    public List<FlightDto> getFlights() {
        var request = new HttpEntity<>(headers);
        return restTemplate.getForObject(apiUsersUrl,
                FlightDtoList.class,
                request).getFlights();
    }

    public FlightDto getFlight(Long id) {
        var request = new HttpEntity<>(headers);
        return restTemplate.getForObject(
                apiUsersUrl+"/"+id,
                FlightDto.class,
                request);
    }

    public FlightDto addFlight(FlightDto flightDto) {
        var request = new HttpEntity<>(flightDto, headers);
        return restTemplate.postForObject(apiUsersUrl,
                request,
                FlightDto.class);
    }

    public FlightDto editFlight(FlightDto flightDto, Long id) {
        var request = new HttpEntity<>(flightDto, headers);
        return restTemplate.postForObject(
                apiUsersUrl +"/" +id,
                request,
                FlightDto.class);
    }

    public String deleteFlight(Long id) {
        restTemplate.delete(apiUsersUrl +"/" + id);
        return "Success delete flight with id: " + id;
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Client.TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
