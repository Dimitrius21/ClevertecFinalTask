package ru.clevertec.finalproj.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import ru.clevertec.finalproj.domain.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.List;
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
class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getCommentByIdTest() throws Exception {
        long id = 11;
        LocalDateTime dt = LocalDateTime.of(2023, 05, 30, 0, 0);
        CommentDto commentDto = new CommentDto(id, dt, "Comment120", "Maria", 1);
        mockMvc.perform(
                        get("/api/comment/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDto)));
    }

    @Test
    void getCommentBySearchTest() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.addAll("field", List.of("text", "username"));
        requestParams.addAll("value", List.of("Comment2", "Sema"));
        mockMvc.perform(get("/api/comment/has")
                        .queryParams(requestParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("[0].text").value("Comment22"));
    }

    @Test
    void createCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto(0, null, "MyComment", "Sema", 1);
        Matcher<Long> matcher = IsNot.not(0L);
        mockMvc.perform(
                        post("/api/comment")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsBytes(commentDto))
                                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6WyJST0xFX1NVQlNDUklCRVIiXSwiaWF0IjoxNjg3Njg1Mjk0LCJzdWIiOiJTZW1hIiwiZXhwIjoxNjkxMjg1Mjk0fQ.8QdJz9r_N6tBJxNV0Q52EK1j9NfCvIa-kTzveR4iCkM"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(matcher))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.username").value(commentDto.getUsername()));
    }

    @Test
    void rejectCreateCommentForJournalistTest() throws Exception {
        CommentDto commentDto = new CommentDto(0, null, "MyComment", "Mark", 1);
        Matcher<Long> matcher = IsNot.not(0L);
        mockMvc.perform(
                        post("/api/comment")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsBytes(commentDto))
                                .header("Authorization", "Basic TWFyazoyMDA="))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateCommentTest() throws Exception {
        int i = new Random().nextInt();
        String newText = "Comment" + i;
        CommentDto commentDto = new CommentDto(2, null, newText, "Sema", 1);
        long id = commentDto.getId();
        mockMvc.perform(
                        put("/api/comment")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsBytes(commentDto))
                                .header("Authorization", "Basic U2VtYTozMDA="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value(newText));
    }

    @Test
    void rejectUpdateCommentForUnAuthenticateUserTest() throws Exception {
        int i = new Random().nextInt();
        String newText = "Comment" + i;
        CommentDto commentDto = new CommentDto(2, null, newText, "Sema", 1);
        long id = commentDto.getId();
        mockMvc.perform(
                        put("/api/comment")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsBytes(commentDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto(0, null, "SomeComment", "Sema", 1);
        String res = mockMvc.perform(
                        post("/api/comment")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsBytes(commentDto))
                                .header("Authorization", "Basic U2VtYTozMDA=")) //subscriber
                .andReturn().getResponse().getContentAsString();
        CommentDto createdDto = mapper.readValue(res, CommentDto.class);
        long id = createdDto.getId();
        mockMvc.perform(
                        delete("/api/comment/{id}", id) // admin
                                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6WyJST0xFX0FETUlOIl0sImlhdCI6MTY4NzUxOTQ4MSwic3ViIjoiYWRtaW4iLCJleHAiOjE2OTExMTk0ODF9.OeBHx7THjO3iHg-LwaAtUcX5y-3hDr0vzuNhf-CuFkI"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(id)));
        mockMvc.perform(
                        get("/tag/{id}", id))
                .andExpect(status().isNotFound());
    }
}