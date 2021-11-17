package com.pgs.client.service;

import com.pgs.client.dto.WeatherDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.weather}")
    private String apiWeatherUrl;

    public WeatherDto getWeatherByCity(String city) {
        return restTemplate.getForObject(
                apiWeatherUrl + "/?city=" + city,
                WeatherDto.class);
    }
}
