package com.pgs.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class FlightDto {
    private Long id;
    private TypeOfFlight type;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String departureAirportIataCode;
    private String arrivalAirportIataCode;

    public enum TypeOfFlight {
        ECONOMY, PREMIUM, BUSINESS
    }
}
