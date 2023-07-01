package ru.clevertec.finalproj.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import ru.clevertec.finalproj.domain.entity.News;
import ru.clevertec.finalproj.repository.CommentRepository;
import ru.clevertec.finalproj.repository.NewsRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckUserByRequestIdTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsRepository newsRepository;

    @Test
    void checkTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/news/1");

        long id = 1L;
        News news = getNews(id);
        when(newsRepository.findById(id)).thenReturn(Optional.of(news));

        CheckUserByRequestId checkUser = new CheckUserByRequestId(commentRepository, newsRepository);
        AuthorizationDecision decision = checkUser.check("David", request);

        Assertions.assertThat(decision.isGranted()).isTrue();
    }

    private News getNews(long id) {
        return new News(id, LocalDateTime.now(), "Event", "NewsText", "David", null);
    }
}
