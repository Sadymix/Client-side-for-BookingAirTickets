package com.pgs.client.service;

import com.pgs.client.component.Client;
import com.pgs.client.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    @Value("${app.get.single.user.url}")
    private String apiUsersUrl;

    public UserDto getSingleUser(Long id) {
        HttpHeaders headers = setHeaders();
        var request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                apiUsersUrl + "/" + id,
                HttpMethod.GET,
                request,
                UserDto.class).getBody();
    }

    public UserDto addUser(UserDto userDto) {
        HttpHeaders headers = setHeaders();
        var request = new HttpEntity<>(userDto, headers);
        return restTemplate.exchange(
                apiUsersUrl,
                HttpMethod.POST,
                request,
                UserDto.class).getBody();
    }

    public UserDto activateUser(Long id) {
        HttpHeaders headers = setHeaders();
        var request = new HttpEntity<>(headers);
        return setUserEnabled(apiUsersUrl+"/"+id+"/activate", request);
    }

    public UserDto deactivateUser(Long id) {
        HttpHeaders headers = setHeaders();
        var request = new HttpEntity<>(headers);
        return setUserEnabled(apiUsersUrl+"/"+id+"/deactivate", request);
    }

    public UserDto setUserRoles(Long id, List<String> roleList) {
        HttpHeaders headers = setHeaders();
        var request = new HttpEntity<>(roleList, headers);
        return restTemplate.exchange(
                apiUsersUrl + "/" + id + "/setRoles",
                HttpMethod.PUT,
                request,
                UserDto.class).getBody();
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Client.TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private UserDto setUserEnabled(String url, HttpEntity request) {
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                UserDto.class).getBody();
    }
}
