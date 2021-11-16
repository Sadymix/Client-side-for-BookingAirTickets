package com.pgs.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class PassengerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String telephone;
}
