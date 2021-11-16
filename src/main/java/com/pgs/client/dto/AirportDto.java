package com.pgs.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class AirportDto {
    private Long id;
    private String code;
    private String name;
    private String country;
}
