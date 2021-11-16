package com.pgs.client.dto.list;

import com.pgs.client.dto.FlightDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class FlightDtoList {

    private List<FlightDto> flights;
}
