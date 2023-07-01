package ru.clevertec.finalproj.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import ru.clevertec.finalproj.controller.openapi.NewsOpenAPI;
import ru.clevertec.finalproj.domain.dto.NewsFullDto;
import ru.clevertec.finalproj.domain.dto.NewsShortDto;
import ru.clevertec.finalproj.domain.entity.News;
import ru.clevertec.finalproj.service.NewsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс реализующий слой контроллера для запросов по News
 */
@RestController
@RequestMapping("/api/news")
@Tag(name = "NewsController", description = "Controller for end-points of entity News")
public class NewsController implements NewsOpenAPI {
    @Autowired
    private NewsService service;

    @GetMapping("/{id}")
    public ResponseEntity<NewsShortDto> getNewsById(@PathVariable long id) {
        NewsShortDto news = service.getById(id);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<NewsFullDto> getNewsWithCommentsById(@PathVariable long id, Pageable pageable) {
        NewsFullDto newsDto = service.getByIdWithComments(id, pageable);
        return new ResponseEntity<>(newsDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<NewsShortDto>> getAllNews(Pageable pageable) {
        List<NewsShortDto> newsList = service.getAll(pageable);
        return new ResponseEntity<>(newsList, HttpStatus.OK);
    }

    @GetMapping("/has")
    public ResponseEntity<List<NewsShortDto>> getNewsBySearch(@RequestParam String[] field,
                                                              @RequestParam String[] value,
                                                              Pageable pageable) {
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < field.length; i++) {
            params.put(field[i], value[i]);
        }
        List<NewsShortDto> newsList = service.searchNewsWith(params, pageable);
        return new ResponseEntity<>(newsList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteNews(@PathVariable long id) {
        service.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NewsShortDto> createNews(@Valid @RequestBody News news) {
        NewsShortDto newsDto = service.create(news);
        return new ResponseEntity<>(newsDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<NewsShortDto> updateNews(@RequestBody News news) {
        NewsShortDto newsDto = service.update(news);
        return new ResponseEntity<>(newsDto, HttpStatus.OK);
    }
}
