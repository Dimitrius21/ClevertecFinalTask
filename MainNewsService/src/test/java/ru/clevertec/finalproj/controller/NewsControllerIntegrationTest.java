package ru.clevertec.finalproj.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import ru.clevertec.finalproj.domain.dto.CommentDto;
import ru.clevertec.finalproj.domain.dto.NewsShortDto;
import ru.clevertec.finalproj.domain.entity.News;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WireMockTest(httpPort = 8090)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NewsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void getNewsByIdTest() throws Exception {
        long id = 1;
        LocalDateTime dt = LocalDateTime.of(2023, 05, 29, 0, 0);
        NewsShortDto newsDto = new NewsShortDto(1, dt, "New BMW", "Presented new BMW 5", "Mark");
        mockMvc.perform(
                        get("/api/news/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(newsDto)));
    }

    @Test
    public void getNewsWithCommentsByIdTest() throws Exception {
        MultiValueMapAdapter requestParams = new MultiValueMapAdapter<>(new HashMap<>());
        requestParams.setAll(Map.of("page", "0", "size", "3"));
        long id = 1;
        mockMvc.perform(
                        get("/api/news/{id}/comments", id)
                                .queryParams(requestParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.comments.length()").value(3));
    }

    @Test
    void getNewsBySearchTest() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.addAll("field", List.of("text", "title"));
        requestParams.addAll("value", List.of("News5", "Event5"));

        mockMvc.perform(get("/api/news/has")
                        .queryParams(requestParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("[0].title").value("Event5"))
                .andExpect(jsonPath("[0].text").value("News5"));
    }

    @Test
    public void getAllNewsTest() throws Exception {
        MultiValueMapAdapter requestParams = new MultiValueMapAdapter<>(new HashMap<>());
        requestParams.setAll(Map.of("page", "0", "size", "3"));

        mockMvc.perform(get("/api/news")
                        .queryParams(requestParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void createNewsTest() throws Exception {
        News news = new News(0, null, "TitleOfNews", "TextOfNews", "Mark", null);
        Matcher<Long> matcher = IsNot.not(0L);

        mockMvc.perform(
                        post("/api/news")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsBytes(news))
                                .header("Authorization", "Basic TWFyazoyMDA="))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(matcher))
                .andExpect(jsonPath("$.text").value(news.getText()))
                .andExpect(jsonPath("$.title").value(news.getTitle()))
                .andExpect(jsonPath("$.username").value(news.getUsername()));
    }

    @Test
    public void updateNewsTest() throws Exception {
        int i = new Random().nextInt();
        String newText = "News" + i;
        String newTitle = "Event" + i;
        long id = 7;
        News news = new News(7, null, newTitle, newText, "Mark", null);

        mockMvc.perform(
                        put("/api/news")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsBytes(news))
                                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6WyJST0xFX0pPVVJOQUxJU1QiXSwiaWF0IjoxNjg3Njg5Mjg1LCJzdWIiOiJNYXJrIiwiZXhwIjoxNjkxMjg5Mjg1fQ.v2bnpfopme1XZBIEY7gRDrX7rG3_NFwj4Rqw6ks3vj0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(newTitle))
                .andExpect(jsonPath("$.text").value(newText));
    }

    @Test
    void deleteNewsTest() throws Exception {
        News news = new News(0, null, "TitleOfNews", "TextOfNews", "Mark", null);
        String res = mockMvc.perform(
                        post("/api/news")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsBytes(news))
                                .header("Authorization", "Basic TWFyazoyMDA="))
                .andReturn().getResponse().getContentAsString();
        CommentDto createdDto = mapper.readValue(res, CommentDto.class);
        long id = createdDto.getId();
        mockMvc.perform(
                        delete("/api/news/{id}", id)
                                .header("Authorization", "Basic TWFyazoyMDA="))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(id)));
        mockMvc.perform(
                        get("/tag/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectDeleteNewsByAnotherJournalistTest() throws Exception {
        long id = 10;
        mockMvc.perform(
                        delete("/api/news/{id}", id)
                                .header("Authorization", "Basic RGF2aWQ6NDAw"))
                .andExpect(status().isForbidden());
    }


/*    @TestConfiguration
    static class TestConfig implements WebMvcConfigurer {

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
        }
    }

    @Import(SpringDataWebAutoConfiguration.class)
    */

}