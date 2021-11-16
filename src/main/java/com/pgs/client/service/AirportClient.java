package com.pgs.client.service;

import com.pgs.client.component.Client;
import com.pgs.client.dto.AirportDto;
import com.pgs.client.dto.list.AirportDtoList;
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
public class AirportClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.airports}")
    private String apiAirportsUrl;
    private static HttpHeaders headers = getHeaders();

    public List<AirportDto> getAirports() {
        var request = new HttpEntity<>(headers);
        return restTemplate.getForObject(
                apiAirportsUrl,
                AirportDtoList.class,
                request).getAirports();
    }

    public AirportDto getAirportById(Long id) {
        var request = new HttpEntity<>(headers);
        return restTemplate.getForObject(
                apiAirportsUrl +"/" + id,
                AirportDto.class,
                request);
    }

    public AirportDto getAirportByCode(){
        var request = new HttpEntity<>(headers);
        return restTemplate.getForObject(apiAirportsUrl+"/",
                AirportDto.class,
                request);
    }

    public AirportDto addAirport(AirportDto airportDto){
        var request = new HttpEntity<>(airportDto, headers);
        return restTemplate.postForObject(apiAirportsUrl,
                request,
                AirportDto.class);
    }

    public AirportDto editAirport(AirportDto airportDto, Long id){
        var request = new HttpEntity<>(airportDto, headers);
        return restTemplate.postForObject(apiAirportsUrl +"/" + id,
                request,
                AirportDto.class);
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Client.TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
