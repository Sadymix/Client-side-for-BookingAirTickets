package com.pgs.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenDto {
    private String accessToken;
    private String tokenType;
    private String refreshType;
    private Integer expiresIn;
    private String scope;
}
