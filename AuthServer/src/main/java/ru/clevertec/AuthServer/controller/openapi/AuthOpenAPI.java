package ru.clevertec.AuthServer.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.AuthServer.domain.dto.UserDto;
import ru.clevertec.AuthServer.domain.dto.UserMainDto;
import ru.clevertec.AuthServer.domain.dto.UserRespDto;


public interface AuthOpenAPI {

    @Operation(summary = "Create(save) user-subscriber",
            description = "Create(save) submitted user-subscriber")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "User for save",
            content = {@Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful operation", content = {@Content(schema = @Schema(implementation = UserRespDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<UserRespDto> createUser(@Validated @RequestBody UserDto commentDto);

    @Operation(summary = "Create(save) user-journalist",
            description = "Create(save) submitted user-journalist")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "User for save",
            content = {@Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful operation", content = {@Content(schema = @Schema(implementation = UserRespDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Not authorize", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<UserRespDto> createJournalist(@Validated @RequestBody UserDto commentDto);

    @Operation(summary = "Create Jwt-token",
            description = "Create and issue Jwt-token")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "username and password of a user",
            content = {@Content(schema = @Schema(implementation = UserMainDto.class), mediaType = "application/json")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = String.class), mediaType = "text/plain")}),
            @ApiResponse(responseCode = "400", description = "Incorrect body of the request", content = {@Content(schema = @Schema(implementation = String.class), mediaType = "text/plain")}),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = {@Content(schema = @Schema(implementation = String.class), mediaType = "text/plain")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<String> getJwtToken(Authentication authentication);
}
