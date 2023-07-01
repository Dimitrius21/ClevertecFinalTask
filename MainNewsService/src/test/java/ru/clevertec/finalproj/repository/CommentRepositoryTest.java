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
import ru.clevertec.finalproj.domain.entity.Comment;
import ru.clevertec.finalproj.domain.entity.News;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2-alpine")
            .withUsername("user")
            .withPassword("psw")
            .withDatabaseName("test");

    @BeforeAll
    static void startContainers() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void stopContainers() {
        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void findByNewsIdTest() {
        PageRequest pageable = PageRequest.of(1, 5);
        long newsId = 1;
        List<Comment> comments = commentRepository.findByNewsId(newsId, pageable);
        Assertions.assertThat(comments)
                .hasSize(5)
                .flatExtracting(Comment::getId)
                .contains(6L,7L, 8L, 9L, 10L);
    }

    @Test
    void findByIdTest() {
        Comment result = commentRepository.findById(1L).get();
        Assertions.assertThat(result.getId()).isEqualTo(1);
        Assertions.assertThat(result.getText()).isEqualTo("Comment11");
    }

    @Test
    void findAllTest() {
        Sort sort = Sort.by(Sort.Order.asc("text"));
        PageRequest pageable = PageRequest.of(1, 3, sort);
        List<Comment> comments = commentRepository.findAll(pageable).toList();
        Assertions.assertThat(comments)
                .hasSize(3)
                .flatExtracting(Comment::getText)
                .containsExactly("Comment103", "Comment104", "Comment105");
    }

    @Test
    void deleteByIdTest() {
        long id = 10L;
        commentRepository.deleteById(id);
        Optional<Comment> result = commentRepository.findById(id);
        Assertions.assertThat(result.isEmpty()).isTrue();
    }

       @Test
    void saveTest() {
        News news = new News();
        news.setId(1L);
        Comment comment = Comment.builder().text("SimpleComment").username("Masha").news(news).build();
        Comment result = commentRepository.save(comment);
        Assertions.assertThat(result.getId()).isGreaterThan(0);
        Assertions.assertThat(result.getText()).isEqualTo(comment.getText());
        Assertions.assertThat(result.getUsername()).isEqualTo(comment.getUsername());
        Assertions.assertThat(result.getNews().getId()).isEqualTo(news.getId());
    }

}