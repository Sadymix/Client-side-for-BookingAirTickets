package com.pgs.client.component;

import com.pgs.client.dto.UserDto;
import com.pgs.client.service.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
@Profile("smoke-test")
@RequiredArgsConstructor
public class Client {

    private final UserClient userClient;

    public static String TOKEN;
    private static UserDto userDto = UserDto.builder()
            .username("user0")
            .password("pass0")
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .enabled(true)
            .roles(List.of("USER"))
            .build();

    @PostConstruct
    public void postConstruct() {
        var singleUser = userClient.getSingleUser(1L);
        log.info("Single User: {}" , singleUser);
        var addUser = userClient.addUser(userDto);
        log.info("Added User: {}", addUser);
        var deactivateUser = userClient.deactivateUser(1L);
        log.info("Deactivate User: {}", deactivateUser);
        var activateUser = userClient.activateUser(1L);
        log.info("Activated User: {}", activateUser);
        var setRolesUser = userClient.setUserRoles(1L, List.of("USER", "STAFF"));
        log.info("Set Roles User: {}", setRolesUser);
    }
}