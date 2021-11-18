package com.pgs.client.service;

import com.pgs.client.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.user}")
    private String apiUsersUrl;

    public UserDto getSingleUser(Long id) {
        return restTemplate.getForObject(
                apiUsersUrl + "/" + id,
                UserDto.class);
    }

    public UserDto addUser(UserDto userDto) {
        var request = new HttpEntity<>(userDto);
        return restTemplate.postForObject(
                apiUsersUrl,
                request,
                UserDto.class);
    }

    public UserDto activateUser(Long id) {
        return setUserEnabled(apiUsersUrl + "/" + id + "/activate");
    }

    public UserDto deactivateUser(Long id) {
        return setUserEnabled(apiUsersUrl + "/" + id + "/deactivate");
    }

    public UserDto setUserRoles(Long id, List<String> roleList) {
        var request = new HttpEntity<>(roleList);
        return restTemplate.exchange(
                apiUsersUrl + "/" + id + "/setRoles",
                HttpMethod.PUT,
                request,
                UserDto.class).getBody();
    }

    private UserDto setUserEnabled(String url) {
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                UserDto.class).getBody();
    }
}
