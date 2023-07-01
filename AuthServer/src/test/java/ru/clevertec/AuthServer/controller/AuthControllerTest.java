package ru.clevertec.AuthServer.controller;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getJwtTokenTest() throws Exception {
        String requestBody = "{\"username\": \"admin\", \"password\": 100}";
        String token = mockMvc.perform(
                        post("/auth/jwt")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(requestBody))
                .andReturn().getResponse().getContentAsString();
        String payloadBase64 = token.split("\\.")[1];
        String payload = new String(Base64.getDecoder().decode(payloadBase64));
        Assertions.assertThat(payload).contains("admin", "ROLE_ADMIN");
    }

    @Test
    void createUserTest() throws Exception {
        Matcher<Long> matcher = IsNot.not(0L);
        int i = new Random().nextInt();
        String user = "user" + i;
        String email = user + "@mail.com";
        String requestBody = String.format("{\"username\": \"%s\", \"password\": \"123456\", \"email\": \"%s\"}", user, email);
        mockMvc.perform(post("/auth/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(matcher))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.username").value(user));
    }

    @Test
    void createJournalistTest() throws Exception {
        Matcher<Long> matcher = IsNot.not(0L);
        int i = new Random().nextInt();
        String journalist = "journalist" + i;
        String email = journalist + "@mail.com";
        String requestBody = String.format("{\"username\": \"%s\", \"password\": \"123456\", \"email\": \"%s\"}", journalist, email);
        mockMvc.perform(post("/auth/journalist")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .header("Authorization", "Basic YWRtaW46MTAw"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(matcher))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.username").value(journalist));
    }

    @Test
    void rejectToCreateJournalistTest() throws Exception {
        Matcher<Long> matcher = IsNot.not(0L);
        int i = new Random().nextInt();
        String journalist = "journalist" + i;
        String email = journalist + "@mail.com";
        String requestBody = String.format("{\"username\": \"%s\", \"password\": \"123456\", \"email\": \"%s\"}", journalist, email);
        mockMvc.perform(post("/auth/journalist")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isForbidden());
    }
}