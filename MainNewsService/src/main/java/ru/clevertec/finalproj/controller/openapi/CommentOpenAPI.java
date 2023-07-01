package ru.clevertec.finalproj.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.finalproj.domain.dto.CommentDto;

import java.util.List;

public interface CommentOpenAPI {

    @Operation(summary = "Find Comment by ID",
            description = "Get a Comment object by specifying its id. ")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Comment Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = CommentDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Content hasn't found for submitted ID", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<CommentDto> getCommentById(@PathVariable long id);

    @Operation(summary = "Delete Comment by ID",
            description = "Delete a note about a Comment by specifying ID. ")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Comment Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = Long.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity deleteComment(@PathVariable long id);

    @Operation(summary = "Find Comment by value of its fields",
            description = "Find Comment by value of required its fields ")
    @Parameter(in = ParameterIn.QUERY, name = "field", required = true, description = "Name of Comment's field that take part in search (except ID)")
    @Parameter(in = ParameterIn.QUERY, name = "value", required = true, description = "Value of above field that searching")
    @Parameter(name = "pageable", hidden = true)
    @PageableAsQueryParam
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CommentDto.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<List<CommentDto>> getCommentBySearch(@RequestParam String[] field,
                                                               @RequestParam String[] value,
                                                               Pageable pageable);
    @Operation(summary = "Create(save) Comment",
            description = "Create(save) submitted Comment.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Comment for save",
            content = {@Content(schema = @Schema(implementation = CommentDto.class), mediaType = "application/json")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful operation", content = {@Content(schema = @Schema(implementation = CommentDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto);

    @Operation(summary = "Update Comment",
            description = "Update Comment to submitted one.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Comment for update",
            content = {@Content(schema = @Schema(implementation = CommentDto.class), mediaType = "application/json")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = CommentDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "There's no Comment with submitted ID for update", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto);
}
