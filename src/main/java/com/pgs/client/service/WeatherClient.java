package com.pgs.client.service;

import com.pgs.client.component.Client;
import com.pgs.client.dto.WeatherDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.weather}")
    private String apiWeatherUrl;

    public WeatherDto getWeatherByCity() {
        return restTemplate.getForObject(
                apiWeatherUrl+"/",
                WeatherDto.class);
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Client.TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
