package com.pgs.client.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String grant_type;
}
