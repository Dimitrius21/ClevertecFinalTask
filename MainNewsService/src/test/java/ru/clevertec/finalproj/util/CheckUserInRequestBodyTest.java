package ru.clevertec.finalproj.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;

class CheckUserInRequestBodyTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void checkTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String commentBody = "{\"id\": 0, \"text\": \"any\", \"username\": \"David\", \"newsId\": 1}";
        request.setContent(commentBody.getBytes());
        request.setRequestURI("/api/comment");
        CheckUserInRequestBody checkUser = new CheckUserInRequestBody(objectMapper);
        AuthorizationDecision decision = checkUser.check("David", request);
        Assertions.assertThat(decision.isGranted()).isTrue();
    }

    @Test
    void checkNotEqualsTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String newsBody = "{\"id\": 0, \"title\": \"any\", \"text\": \"any\", \"username\": \"Mark\"}";
        request.setContent(newsBody.getBytes());
        request.setRequestURI("/api/news");
        CheckUserInRequestBody checkUser = new CheckUserInRequestBody(objectMapper);
        AuthorizationDecision decision = checkUser.check("David", request);
        Assertions.assertThat(decision.isGranted()).isFalse();
    }

}