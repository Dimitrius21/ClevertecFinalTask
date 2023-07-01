package ru.clevertec.finalproj.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.List;
import java.util.Map;

@SpringBootTest
class JwtCustomDecoderTest {

    @Autowired
    JwtDecoder decoder;

    @Test
    void decodeTest() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6WyJST0xFX0FETUlOIl0sImlhdCI6MTY4NzUxOTQ4MSwic3ViIjoiYWRtaW4iLCJleHAiOjE2OTExMTk0ODF9.OeBHx7THjO3iHg-LwaAtUcX5y-3hDr0vzuNhf-CuFkI";
        Jwt jwt = decoder.decode(token);
        Assertions.assertThat(jwt.getSubject()).isEqualTo("admin");
        Map map = jwt.getClaims();
        Assertions.assertThat(map.get("scope")).isEqualTo(List.of("ROLE_ADMIN"));
    }
}