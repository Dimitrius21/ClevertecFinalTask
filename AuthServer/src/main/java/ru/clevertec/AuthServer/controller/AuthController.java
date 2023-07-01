package ru.clevertec.AuthServer.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.AuthServer.controller.openapi.AuthOpenAPI;
import ru.clevertec.AuthServer.domain.dto.UserDto;
import ru.clevertec.AuthServer.domain.dto.UserRespDto;
import ru.clevertec.AuthServer.service.AuthService;
import ru.clevertec.AuthServer.utils.UserRoles;

/**
 * Класс реализующий слой Controller приложения
 */
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "AuthController", description = "Controller for end-points of this app")
public class AuthController implements AuthOpenAPI {

    @Autowired
    private AuthService authService;

    @PostMapping("/jwt")
    public ResponseEntity<String> getJwtToken(@Autowired Authentication authentication){
        String jwtToken = authService.createJwtToken(authentication);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<UserRespDto> createUser(@Valid @RequestBody UserDto userData){
        UserRespDto user = authService.createUser(userData, UserRoles.ROLE_SUBSCRIBER);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/journalist")
    public ResponseEntity<UserRespDto> createJournalist(@Valid @RequestBody UserDto userData){
        UserRespDto user = authService.createUser(userData, UserRoles.ROLE_JOURNALIST);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

}
