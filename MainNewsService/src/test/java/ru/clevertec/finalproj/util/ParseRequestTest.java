package ru.clevertec.finalproj.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class ParseRequestTest {

    static MockHttpServletRequest request = new MockHttpServletRequest();

    @BeforeAll
    static void init(){
        request.setRequestURI("/api/news/5");
    }

    @Test
    void getIdTest() {
        long id = ParseRequest.getId(request);
        Assertions.assertThat(id).isEqualTo(5);
    }

    @Test
    void getCorrespondedRoleTest() {
        String role = ParseRequest.getCorrespondedRole(request);
        Assertions.assertThat(role).isEqualTo("ROLE_JOURNALIST");
    }

    @Test
    void getEntityNameTest() {
        String entity = ParseRequest.getEntityName(request);
        Assertions.assertThat(entity).isEqualTo("news");
    }
}