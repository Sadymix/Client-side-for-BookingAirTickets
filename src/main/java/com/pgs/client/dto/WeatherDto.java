package com.pgs.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class WeatherDto {
    private String temperature;
    private String humidity;
    private String pressure;
    private String windSpeed;
}
