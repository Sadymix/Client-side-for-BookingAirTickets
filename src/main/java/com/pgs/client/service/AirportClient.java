package com.pgs.client.service;

import com.pgs.client.dto.AirportDto;
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
public class AirportClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.airports}")
    private String apiAirportsUrl;

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
        var request = new HttpEntity<>(airportDto);
        return restTemplate.postForObject(
                apiAirportsUrl,
                request,
                AirportDto.class);
    }

    public AirportDto editAirport(AirportDto airportDto, Long id) {
        var request = new HttpEntity<>(airportDto);
        return restTemplate.exchange(
                apiAirportsUrl + "/" + id,
                HttpMethod.PUT,
                request,
                AirportDto.class).getBody();
    }
}
