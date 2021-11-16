package com.pgs.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class ReservationDto {
    private Long id;
    private Long flightId;
    private ReservationStatus status;
    private List<PassengerDto> passengers;
    private Long userId;

    public enum ReservationStatus {
        IN_PROGRESS, REALIZED, CANCELED
    }
}
