package com.pgs.client.service;

import com.pgs.client.dto.WeatherDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherClientTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private WeatherClient weatherClient;

    private static final String URL ="http://localhost:8080/api/weather";
    private static final WeatherDto WEATHER = WeatherDto.builder()
            .temperature("15°C")
            .humidity("70%")
            .pressure("1012hPa")
            .windSpeed("6 km/h")
            .build();
    @Test
    void testGetWeatherByCity() {
        ReflectionTestUtils.setField(weatherClient, "apiWeatherUrl", URL);
        when(restTemplate.getForObject(
                eq(URL+"/?city=Warsaw"),
                eq(WeatherDto.class)))
                .thenReturn(WEATHER);
        var weather = weatherClient.getWeatherByCity("Warsaw");
        assertThat(weather).isNotNull();
        assertThat(weather).isEqualTo(WEATHER);
    }
}