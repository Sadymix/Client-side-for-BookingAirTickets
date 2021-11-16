package com.pgs.client.dto.list;

import com.pgs.client.dto.AirportDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class AirportDtoList {
    private List<AirportDto> airports;
}
