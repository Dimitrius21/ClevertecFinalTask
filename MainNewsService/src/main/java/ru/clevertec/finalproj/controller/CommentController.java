package ru.clevertec.finalproj.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.finalproj.controller.openapi.CommentOpenAPI;
import ru.clevertec.finalproj.domain.dto.CommentDto;
import ru.clevertec.finalproj.service.CommentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс реализующий слой контроллера для запросов по Comment
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/comment")
@Tag(name = "CommentController", description = "Controller for end-points of entity Comment")
public class CommentController implements CommentOpenAPI {
    @Autowired
    CommentService service;

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable long id) {
        CommentDto comment = service.getById(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteComment(@PathVariable long id) {
        service.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/has")
    public ResponseEntity<List<CommentDto>> getCommentBySearch(@RequestParam String[] field,
                                                               @RequestParam String[] value,
                                                               Pageable pageable) {
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < field.length; i++) {
            params.put(field[i], value[i]);
        }
        List<CommentDto> commentList = service.searchCommentWith(params, pageable);
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto) {
        CommentDto commentDtoResult = service.create(commentDto);
        return new ResponseEntity<>(commentDtoResult, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentDto commentDto) {
        CommentDto commentDtoResult = service.update(commentDto);
        return new ResponseEntity<>(commentDtoResult, HttpStatus.OK);
    }
}
