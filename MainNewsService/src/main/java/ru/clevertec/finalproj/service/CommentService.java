package ru.clevertec.finalproj.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.exceptionhandler.exception.NotValidRequestParametersException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;
import ru.clevertec.finalproj.domain.dto.CommentDto;
import ru.clevertec.finalproj.domain.entity.Comment;
import ru.clevertec.finalproj.domain.entity.News;
import ru.clevertec.finalproj.repository.CommentRepository;
import ru.clevertec.finalproj.repository.NewsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Класс реализующий операции из слоя Сервис для Comment
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CommentService {

    @Autowired
    private CommentRepository repo;

    @Autowired
    private NewsRepository newsRepo;

    /**
     * Найти Comment по его id
     *
     * @param id Comment
     * @return объект CommentDto с данными найденного Comment
     */
    public CommentDto getById(long id) {
        log.info("called method  getById in CommentService, id={}", id);
        Optional<Comment> comment = repo.findById(id);
        if (comment.isEmpty()) {
            log.info("Error ResourceNotFountException for id={}", id);
            throw new ResourceNotFountException(String.format("Comment with such id(%s) is absent", id), "id-" + id);
        }
        return CommentDto.toCommentDto(comment.get());
    }

    /**
     * Поиск Comments по различным параметрам text и/или username
     *
     * @param params   - мапа, содержащая в key название поля для поиска, в value - значение дл я поиска
     * @param pageable - данные для пагинации и сортировки
     * @return - список CommentDto - найденные Comments по условиям поиска
     */
    public List<CommentDto> searchCommentWith(Map<String, String> params, Pageable pageable) {
        log.info("called method searchCommentWith in CommentService, params: {}, pageable: {}", params, pageable);
        Comment comment = new Comment();
        String field;
        int qtityFieldInSearch = 0;
        ExampleMatcher commentExampleMatcher = ExampleMatcher.matchingAll().withIgnoreNullValues();
        if ((field = params.get("username")) != null) {
            comment.setUsername(field);
            qtityFieldInSearch++;
            commentExampleMatcher = commentExampleMatcher.withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        }
        if ((field = params.get("text")) != null) {
            comment.setText(field);
            qtityFieldInSearch++;
            commentExampleMatcher = commentExampleMatcher.withMatcher("text", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        }
        if (qtityFieldInSearch != params.size()) {
            throw new NotValidRequestParametersException("Incorrect fields for search");
        }
        commentExampleMatcher = commentExampleMatcher.withIgnorePaths("id");
        Example<Comment> newsExample = Example.of(comment, commentExampleMatcher);
        List<Comment> commentList = repo.findAll(newsExample, pageable).toList();
        List<CommentDto> commentDtoList = commentList.stream().map(CommentDto::toCommentDto).toList();
        return commentDtoList;
    }

    /**
     * Содать(сохранить) Comment
     *
     * @param commentDto - объект из запроса содержащий данные для Comment
     * @return - сохраненный Comment ввиде объекта CommentDto с присвоенным id
     */
    public CommentDto create(CommentDto commentDto) {
        long newsId = commentDto.getNewsId();
        log.info("called method create in CommentService, comment: {}", commentDto);
        Optional<News> news = newsRepo.findById(newsId);
        if (news.isEmpty()) {
            log.info("Error RequestBodyIncorrectException for News id={}", newsId);
            throw new RequestBodyIncorrectException(String.format("News with id = %d is absent", newsId), "id-" + newsId);
        }
        Comment comment = Comment.builder()
                .time(LocalDateTime.now())
                .text(commentDto.getText())
                .username(commentDto.getUsername())
                .news(news.get())
                .build();
        comment = repo.save(comment);
        return CommentDto.toCommentDto(comment);
    }

    /**
     * Обновить Comment - поле text
     *
     * @param commentDto - объект из запроса содержащий данные для изменяемого Comment
     * @return сохраненный измененный Comment ввиде объекта CommentDto
     */
    public CommentDto update(CommentDto commentDto) {
        Comment comment = CommentDto.toComment(commentDto);
        log.info("called method update in CommentService, comment: {}", comment);
        long commentId = comment.getId();
        Comment commentInDb = repo.findById(commentId).orElseThrow(
                () -> {
                    log.info("Error RequestBodyIncorrectException for Comment id={} - is absent", commentId);
                    return new RequestBodyIncorrectException(String.format("Comment with id = %d is absent", commentId), "id-" + commentId);
                });
        if (commentInDb.getUsername().equals(comment.getUsername()) && commentInDb.getNews().getId() == comment.getNews().getId()) {
            commentInDb.setText(comment.getText());
            Comment result = repo.save(commentInDb);
            return CommentDto.toCommentDto(result);
        } else {
            log.info("Error RequestBodyIncorrectException for Comment id={}, Username/newsId don't correspond", commentId);
            throw new RequestBodyIncorrectException("Username/newsId don't correspond data in DB.");
        }
    }

    /**
     * Удалить Comment по его id
     *
     * @param id Comment
     * @return id удаленного Comment
     */
    public long deleteById(long id) {
        log.info("called method  deleteById in CommentService, id={}", id);
        repo.deleteById(id);
        return id;
    }

}
