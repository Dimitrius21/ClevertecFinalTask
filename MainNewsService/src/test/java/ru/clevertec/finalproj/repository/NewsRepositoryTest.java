package ru.clevertec.finalproj.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.finalproj.domain.entity.News;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class NewsRepositoryTest {

    @Autowired
    NewsRepository newsRepository;

    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2-alpine")
            .withUsername("user")
            .withPassword("psw")
            .withDatabaseName("test");

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void startContainers() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void stopContainers() {
        postgreSQLContainer.stop();
    }

    @Test
    void findByIdTest() {
        News result = newsRepository.findById(1L).get();
        Assertions.assertThat(result.getId()).isEqualTo(1);
        Assertions.assertThat(result.getTitle()).isEqualTo("New BMW");
    }

    @Test
    void findAllTest() {
        Sort sort = Sort.by(Sort.Order.asc("title"));
        PageRequest pageable = PageRequest.of(1, 3, sort);
        List<News> newsList = newsRepository.findAll(pageable).toList();
        Assertions.assertThat(newsList)
                .hasSize(3)
                .flatExtracting(News::getTitle)
                .containsExactly("Event7", "Event8", "Event9");
    }

    @Test
    void saveTest() {
        News news = new News(0, LocalDateTime.now(), "SomeEvent", "SomeText", "Anna", null);
        News result = newsRepository.save(news);
        Assertions.assertThat(result.getId()).isGreaterThan(0);
        Assertions.assertThat(result.getText()).isEqualTo(news.getText());
        Assertions.assertThat(result.getTitle()).isEqualTo(news.getTitle());
    }

    @Test
    void deleteByIdTest() {
        News news = new News(0, LocalDateTime.now(), "SomeEvent", "SomeText", "Anna", null);
        News newNews = newsRepository.save(news);
        long id = newNews.getId();
        newsRepository.deleteById(id);
        Optional<News> result = newsRepository.findById(id);
        Assertions.assertThat(result.isEmpty()).isTrue();
    }
}