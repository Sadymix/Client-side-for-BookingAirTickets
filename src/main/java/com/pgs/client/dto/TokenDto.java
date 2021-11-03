package com.pgs.client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private String access_token;
    private String token_type;
    private String refresh_type;
    private Integer expires_in;
    private String scope;
}
