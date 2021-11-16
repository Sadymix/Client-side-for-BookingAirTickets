package com.pgs.client.service;

import com.pgs.client.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    @Value("${app.url.user}")
    private String apiUsersUrl;
    private static HttpHeaders headers = getHeaders();

    public UserDto getSingleUser(Long id) {
        return restTemplate.getForObject(
                apiUsersUrl + "/" + id,
                UserDto.class);
    }

    public UserDto addUser(UserDto userDto) {
        var request = new HttpEntity<>(userDto, headers);
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
        var request = new HttpEntity<>(roleList, headers);
        return restTemplate.postForObject(
                apiUsersUrl + "/" + id + "/setRoles",
                request,
                UserDto.class);
    }

    private UserDto setUserEnabled(String url) {
        var request = new HttpEntity<>(headers);
        return restTemplate.postForObject(
                url,
                request,
                UserDto.class);
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
