package com.pgs.client.service;

import com.pgs.client.component.Client;
import com.pgs.client.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserClientTest {

    @Captor
    private ArgumentCaptor<HttpEntity<UserDto>> requestCaptor;
    @Mock
    private ResponseEntity<UserDto> responseEntity;

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private UserClient userClient;

    private static final String URL = "http://localhost:8080/api/users";
    private static final UserDto USER_DTO = UserDto.builder()
            .id(1L)
            .username("user1")
            .password("password1")
            .roles(List.of("ADMIN"))
            .build();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userClient, "apiUsersUrl", URL);
        Client.TOKEN = "qwerty";
        when(responseEntity.getBody()).thenReturn(USER_DTO);
    }

    @Test
    void testGetSingleUser() {
        when(restTemplate.exchange(eq(URL + "/1"), eq(HttpMethod.GET), any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(responseEntity);

        userClient.getSingleUser(1L);

        verify(restTemplate).exchange(anyString(), any(HttpMethod.class), requestCaptor.capture(), any(Class.class));
        var request = requestCaptor.getValue();

        assertThat(request).isNotNull();
        assertThat(request.getHeaders().getValuesAsList(HttpHeaders.AUTHORIZATION))
                .hasSize(1)
                .contains("Bearer " + Client.TOKEN);
    }

    @Test
    void testAddUser() {
        when(restTemplate.exchange(eq(URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(responseEntity);

        userClient.addUser(USER_DTO);

        verify(restTemplate).exchange(anyString(), any(HttpMethod.class), requestCaptor.capture(), any(Class.class));
        var request = requestCaptor.getValue();

        assertThat(request.getBody()).isEqualTo(USER_DTO);
    }

    private HttpEntity<Object> setUpGetSingleUserRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Client.TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return new HttpEntity<>(headers);
    }
}