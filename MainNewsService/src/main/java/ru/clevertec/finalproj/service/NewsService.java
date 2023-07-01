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
import ru.clevertec.finalproj.domain.dto.NewsFullDto;
import ru.clevertec.finalproj.domain.dto.NewsShortDto;
import ru.clevertec.finalproj.domain.entity.Comment;
import ru.clevertec.finalproj.domain.entity.News;
import ru.clevertec.finalproj.repository.CommentRepository;
import ru.clevertec.finalproj.repository.NewsRepository;
import ru.clevertec.finalproj.util.NewsMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Класс реализующий операции из слоя Сервис для News
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class NewsService {
    @Autowired
    private NewsRepository repo;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private NewsMapper mapper;

    /**
     * Найти News по его id
     *
     * @param id News
     * @return объект NewsShortDto(без Comments) с данными найденного News
     */
    public NewsShortDto getById(long id) {
        log.info("called method  getById in NewsService, id={}", id);
        Optional<News> news = repo.findById(id);
        if (news.isEmpty()) {
            log.info("Error ResourceNotFountException for id={}", id);
            throw new ResourceNotFountException(String.format("News with such id(%s) is absent", id), "id-" + id);
        }
        NewsShortDto newsDto = mapper.toNewsShortDto(news.get());
        return newsDto;
    }

    /**
     * Создать(сохранить) News
     *
     * @param news - объект из запроса содержащий данные для News
     * @return - сохраненный News ввиде объекта NewsShortDto с присвоенным id
     */
    public NewsShortDto create(News news) {
        log.info("called method create in NewsService, news: {}", news);
        news.setTime(LocalDateTime.now());
        news.setId(0);
        News newsSaved = repo.save(news);
        return mapper.toNewsShortDto(newsSaved);
    }

    /**
     * Обновить News - поле title и/или text
     *
     * @param news объект из запроса содержащий данные для News
     * @return измененный сохраненный News ввиде объекта NewsShortDto
     */
    public NewsShortDto update(News news) {
        log.info("called method update in NewsService, news: {}", news);
        long id = news.getId();
        News newsInDb = repo.findById(id).orElseThrow(
                () -> {
                    log.info("Error RequestBodyIncorrectException for News id={} - is absent", id);
                    return new RequestBodyIncorrectException(String.format("News with id = %d is absent", id), "id-" + id);
                });
        String title = news.getTitle();
        String text = news.getText();
        boolean existChange = false;
        if ((title != null) && !title.isEmpty()) {
            newsInDb.setTitle(title);
            existChange = true;
        }
        if ((text != null) && !text.isEmpty()) {
            newsInDb.setText(text);
            existChange = true;
        }
        if (existChange) {
            News result = repo.save(newsInDb);
            return mapper.toNewsShortDto(result);
        } else {
            throw new RequestBodyIncorrectException("There is now data for change");
        }
    }

    /**
     * Удалить News по его id
     *
     * @param id News в хранилище
     * @return id удаленного News
     */
    public long deleteById(long id) {
        log.info("called method  deleteById in NewsService, id={}", id);
        repo.deleteById(id);
        return id;
    }

    /**
     * Получить список всех News
     *
     * @param pageable - данные для пагинации и сортировки
     * @return список найденных News ввиде NewsShortDto (News без Comments)
     */
    public List<NewsShortDto> getAll(Pageable pageable) {
        log.info("called method getAll in NewsService");
        List<News> newsList = repo.findAll(pageable).toList();
        List<NewsShortDto> onlyNews = newsList.stream().map(n -> mapper.toNewsShortDto(n)).toList();
        return onlyNews;
    }

    /**
     * Найти News по его id
     *
     * @param id News
     * @return объектNewsFullDto(c Comments) с данными найденного News
     */

    public NewsFullDto getByIdWithComments(long id, Pageable pageable) {
        log.info("called method getByIdWithComments in NewsService,  id={}", id);
        Optional<News> newsOptional = repo.findById(id);
        if (newsOptional.isEmpty()) {
            throw new ResourceNotFountException(String.format("News with such id(%s) is absent", id), "id-" + id);
        }
        News news = newsOptional.get();
        List<Comment> commentsForNews = commentRepo.findByNewsId(id, pageable);
        news.setComments(commentsForNews);
        return NewsFullDto.toNewsFullDto(news);
    }

    /**
     * Поиск News по различным параметрам title/text/username - в любом сочетании
     *
     * @param params   - мапа, содержащая в key название поля для поиска, в value - значение для поиска
     * @param pageable - данные для пагинации и сортировки
     * @return - список NewsShortDto - найденные News по условиям поиска
     */
    public List<NewsShortDto> searchNewsWith(Map<String, String> params, Pageable pageable) {
        log.info("called method searchCommentWith in NewsService, params: {}, pageable: {}", params, pageable);
        News news = new News();
        String field;
        int qtityFieldInSearch = 0;
        ExampleMatcher newsExampleMatcher = ExampleMatcher.matchingAll().withIgnoreNullValues();
        if ((field = params.get("title")) != null) {
            news.setTitle(field);
            qtityFieldInSearch++;
            newsExampleMatcher = newsExampleMatcher.withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        }
        if ((field = params.get("text")) != null) {
            news.setText(field);
            qtityFieldInSearch++;
            newsExampleMatcher = newsExampleMatcher.withMatcher("text", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        }
        if ((field = params.get("username")) != null) {
            qtityFieldInSearch++;
            news.setUsername(field);
            newsExampleMatcher = newsExampleMatcher.withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        }
        if (qtityFieldInSearch!= params.size()) {
            throw new NotValidRequestParametersException("Incorrect fields for search");
        }
        newsExampleMatcher = newsExampleMatcher.withIgnorePaths("id");
        Example<News> newsExample = Example.of(news, newsExampleMatcher);
        List<News> newsList = repo.findAll(newsExample, pageable).toList();
        List<NewsShortDto> newsShortDtoList = newsList.stream().map(n -> mapper.toNewsShortDto(n)).toList();
        return newsShortDtoList;
    }

}
