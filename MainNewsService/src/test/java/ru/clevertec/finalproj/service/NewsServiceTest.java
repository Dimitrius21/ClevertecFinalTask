package ru.clevertec.finalproj.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsRepository newsRepository;

    @Autowired
    @Spy
    private NewsMapper mapper;

    @InjectMocks
    NewsService newsService;

    @Test
    void getByIdTest() {
        long id = 1L;
        News news = getNews(id);
        when(newsRepository.findById(id)).thenReturn(Optional.of(news));
        NewsShortDto newsDto = mapper.toNewsShortDto(news);

        NewsShortDto result = newsService.getById(id);

        Assertions.assertThat(result).isEqualTo(newsDto);
    }


    @Test
    void createTest() {
        News news = getNews(0);
        News newsAfterSave = getNews(1);
        when(newsRepository.save(any(News.class))).thenReturn(newsAfterSave);
        NewsShortDto newsDto = mapper.toNewsShortDto(newsAfterSave);

        NewsShortDto result = newsService.create(news);

        Assertions.assertThat(result).isEqualTo(newsDto);
    }

    @Test
    void updateTest() {
        long id = 1;
        News newsInDb = getNews(id);
        when(newsRepository.findById(id)).thenReturn(Optional.of(newsInDb));

        News newsInRequest = getNews(id);
        newsInRequest.setTitle("NewTitle");
        newsInRequest.setText("NewText");

        News newsSaved = getNews(id);
        newsSaved.setTime(newsInDb.getTime());
        newsSaved.setTitle(newsInRequest.getTitle());
        newsSaved.setText(newsInRequest.getText());
        when(newsRepository.save(newsSaved)).thenReturn(newsSaved);
        NewsShortDto newsDto = mapper.toNewsShortDto(newsSaved);

        NewsShortDto result = newsService.update(newsInRequest);

        Assertions.assertThat(result).isEqualTo(newsDto);
    }

    @Test
    void deleteByIdTest() {
        long id = 20;

        long result = newsService.deleteById(id);

        verify(newsRepository).deleteById(id);
        Assertions.assertThat(result).isEqualTo(id);
    }

    @Test
    void getAll() {
        PageRequest pageable = PageRequest.of(0, 2);
        News news1 = getNews(1);
        News news2 = getNews(2);
        List<News> newsList = List.of(news1, news2);
        Page<News> pageOfNews = new PageImpl<>(newsList);
        when(newsRepository.findAll(pageable)).thenReturn(pageOfNews);

        List<NewsShortDto> result = newsService.getAll(pageable);

        List<NewsShortDto> newsDto = newsList.stream().map(mapper::toNewsShortDto).toList();
        Assertions.assertThat(result).hasSize(2).isEqualTo(newsDto);

    }

    @Test
    void getByIdWithCommentsTest() {
        long id = 1L;
        News news = getNews(id);
        when(newsRepository.findById(id)).thenReturn(Optional.of(news));

        Comment comment1 = Comment.builder().id(1).time(LocalDateTime.now()).text("Comment1").username("user1").news(news).build();
        Comment comment2 = Comment.builder().id(2).time(LocalDateTime.now()).text("Comment2").username("user2").news(news).build();

        List<Comment> commentList = List.of(comment1, comment2);
        PageRequest pageable = PageRequest.of(0, 2);
        when(commentRepository.findByNewsId(id, pageable)).thenReturn(commentList);
        news.setComments(commentList);
        NewsFullDto expNews = NewsFullDto.toNewsFullDto(news);

        NewsFullDto result = newsService.getByIdWithComments(id, pageable);

        Assertions.assertThat(result).isEqualTo(expNews);
    }

    @Test
    void searchNewsWithTest() {
        PageRequest pageable = PageRequest.of(0, 3);
        String field1 = "title";
        String field2 = "text";
        String val1 = "Event";
        String val2 = "News";
        Map<String, String> params = Map.of(field1, val1, field2, val2);

        News news = new News();
        news.setText(val2);
        news.setTitle(val1);
        ExampleMatcher newsExampleMatcher = ExampleMatcher.matchingAll().withIgnoreNullValues();
        newsExampleMatcher = newsExampleMatcher.withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        newsExampleMatcher = newsExampleMatcher.withMatcher("text", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        newsExampleMatcher = newsExampleMatcher.withIgnorePaths("id");
        Example<News> newsExample = Example.of(news, newsExampleMatcher);
        Page<News> pageOfNews = new PageImpl<>(List.of(news));
        when(newsRepository.findAll(newsExample, pageable)).thenReturn(pageOfNews);

        List<NewsShortDto> result = newsService.searchNewsWith(params, pageable);

        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(news.getId());
    }

    private News getNews(long id) {
        return new News(id, LocalDateTime.now(), "Event", "NewsText", "admin", null);
    }
}