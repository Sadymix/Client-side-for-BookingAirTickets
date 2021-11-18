package com.pgs.client.service;

import com.pgs.client.dto.PassengerDto;
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
public class PassengerClient {

    private final RestTemplate restTemplate;
    @Value("${app.url.passengers}")
    private String apiPassengersUrl;

    public List<PassengerDto> getPassengers() {
        var responseType = new ParameterizedTypeReference<List<PassengerDto>>() {
        };
        return restTemplate.exchange(
                apiPassengersUrl,
                HttpMethod.GET,
                null,
                responseType).getBody();
    }

    public PassengerDto getSinglePassenger(Long id) {
        return restTemplate.getForObject(
                apiPassengersUrl + "/" + id,
                PassengerDto.class);
    }

    public PassengerDto addPassenger(PassengerDto passengerDto) {
        var request = new HttpEntity<>(passengerDto);
        return restTemplate.postForObject(apiPassengersUrl,
                request,
                PassengerDto.class);
    }

    public PassengerDto editPassenger(PassengerDto passengerDto, Long id) {
        var request = new HttpEntity<>(passengerDto);
        return restTemplate.exchange(
                apiPassengersUrl + "/" + id,
                HttpMethod.PUT,
                request,
                PassengerDto.class).getBody();
    }
}
