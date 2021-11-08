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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("smoke-test")
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
            .username("user1")
            .password("password1")
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .enabled(false)
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

    @Test
    void testActivateUser() {
        when(restTemplate.exchange(eq(URL + "/1/activate"), eq(HttpMethod.PUT),
                any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(responseEntity);
        var userDto = userClient.activateUser(1L);
        verify(restTemplate).exchange(anyString(), any(HttpMethod.class), requestCaptor.capture(), any(Class.class));
        assertEquals(userDto.isEnabled(), USER_DTO.isEnabled());
    }

    @Test
    void testDeactivateUser() {
        when(restTemplate.exchange(eq(URL+"/1/deactivate"), eq(HttpMethod.PUT),
                any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(responseEntity);
        var userDto = userClient.deactivateUser(1L);
        verify(restTemplate).exchange(anyString(), any(HttpMethod.class), requestCaptor.capture(), any(Class.class));
        assertEquals(userDto.isEnabled(), USER_DTO.isEnabled());
    }
    @Test
    void testSetRoles() {
        when(restTemplate.exchange(eq(URL+"/1/setRoles"), eq(HttpMethod.PUT),
                any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(responseEntity);
        var userDto = userClient.setUserRoles(1L, List.of("ADMIN", "USER"));
        verify(restTemplate).exchange(anyString(), any(HttpMethod.class), requestCaptor.capture(), any(Class.class));
        assertEquals(userDto.getRoles(), USER_DTO.getRoles());
    }
}