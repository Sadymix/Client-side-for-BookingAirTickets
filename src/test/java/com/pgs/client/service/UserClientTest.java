package com.pgs.client.service;

import com.pgs.client.component.Client;
import com.pgs.client.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserClientTest {

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
    private static final UserDto USER_DTO1 = UserDto.builder()
            .enabled(false)
            .build();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userClient, "apiUsersUrl", URL);
        Client.TOKEN = "qwerty";
    }

    @Test
    void testGetSingleUser() {
        when(restTemplate.getForObject(eq(URL + "/1"), eq(UserDto.class)))
                .thenReturn(USER_DTO);

        var user = userClient.getSingleUser(1L);

        verify(restTemplate).getForObject(anyString(), any(Class.class));
        assertEquals(user, USER_DTO);
    }

    @Test
    void testAddUser() {
        when(restTemplate.postForObject(eq(URL), any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(USER_DTO);
        var user = userClient.addUser(USER_DTO);
        verify(restTemplate).postForObject(anyString(), any(HttpEntity.class), any(Class.class));
        assertThat(user).isEqualTo(USER_DTO);
    }

    @Test
    void testActivateUser() {
        when(restTemplate.postForObject(eq(URL + "/1/activate"),
                any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(USER_DTO1);
        var user = userClient.activateUser(1L);
        verify(restTemplate).postForObject(anyString(), any(HttpEntity.class), any(Class.class));
        assertEquals(user, USER_DTO1);
    }

    @Test
    void testDeactivateUser() {
        when(restTemplate.postForObject(eq(URL + "/1/deactivate"),
                any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(USER_DTO);
        var user = userClient.deactivateUser(1L);
        verify(restTemplate).postForObject(anyString(), any(HttpEntity.class), any(Class.class));

        assertEquals(user, USER_DTO);
    }

    @Test
    void testSetRoles() {
        when(restTemplate.postForObject(eq(URL + "/1/setRoles"),
                any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(USER_DTO);
        var userDto = userClient.setUserRoles(1L, List.of("ADMIN"));
        verify(restTemplate).postForObject(anyString(), any(HttpEntity.class), any(Class.class));
        assertEquals(userDto.getRoles(), USER_DTO.getRoles());
    }
}
