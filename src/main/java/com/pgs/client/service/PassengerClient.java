package com.pgs.client.service;

import com.pgs.client.dto.PassengerDto;
import com.pgs.client.dto.list.PassengerDtoList;
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
public class PassengerClient {

    private final RestTemplate restTemplate;
    @Value("${app.url.passengers}")
    private String apiPassengersUrl;
    private static HttpHeaders headers = getHeaders();
    public List<PassengerDto> getPassengers() {
        return restTemplate.getForObject(
                apiPassengersUrl,
                PassengerDtoList.class).getPassengers();
    }

    public PassengerDto getSinglePassenger(Long id) {
        return restTemplate.getForObject(
                apiPassengersUrl +"/" +id,
                PassengerDto.class);
    }
    public PassengerDto addPassenger(PassengerDto passengerDto) {
        var request = new HttpEntity<>(passengerDto, headers);
        return restTemplate.postForObject(apiPassengersUrl,
                request,
                PassengerDto.class);
    }

    public PassengerDto editPassenger(PassengerDto passengerDto, Long id) {
        var request = new HttpEntity<>(passengerDto, headers);
        return restTemplate.postForObject(apiPassengersUrl +"/"+id,
                request,
                PassengerDto.class);
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}