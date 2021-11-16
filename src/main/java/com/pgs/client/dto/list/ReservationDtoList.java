package com.pgs.client.dto.list;

import com.pgs.client.dto.ReservationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class ReservationDtoList {
    private List<ReservationDto> reservations;
}
