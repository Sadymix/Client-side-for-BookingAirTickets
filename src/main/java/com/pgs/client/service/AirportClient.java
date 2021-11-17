package com.pgs.client.service;

import com.pgs.client.dto.AirportDto;
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
public class AirportClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.airports}")
    private String apiAirportsUrl;
    private static HttpHeaders headers = getHeaders();

    public List<AirportDto> getAirports() {
        var responseType = new ParameterizedTypeReference<List<AirportDto>>() {
        };
        return restTemplate.exchange(
                apiAirportsUrl,
                HttpMethod.GET,
                null,
                responseType).getBody();
    }

    public AirportDto getAirportById(Long id) {
        return restTemplate.getForObject(
                apiAirportsUrl + "/" + id,
                AirportDto.class);
    }

    public AirportDto getAirportByCode(String code) {
        return restTemplate.getForObject(
                apiAirportsUrl + "?code=" + code,
                AirportDto.class);
    }

    public AirportDto addAirport(AirportDto airportDto) {
        var request = new HttpEntity<>(airportDto, headers);
        return restTemplate.postForObject(
                apiAirportsUrl,
                request,
                AirportDto.class);
    }

    public AirportDto editAirport(AirportDto airportDto, Long id) {
        var request = new HttpEntity<>(airportDto, headers);
        return restTemplate.exchange(
                apiAirportsUrl + "/" + id,
                HttpMethod.PUT,
                request,
                AirportDto.class).getBody();
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
