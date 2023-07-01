package ru.clevertec.finalproj.repository;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.finalproj.client.JwtTokenClient;
import ru.clevertec.finalproj.domain.dto.UserMainDto;


@SpringBootTest
@WireMockTest(httpPort = 8090)
@ActiveProfiles("test")
public class RequestWireMockTest {

/*    @RegisterExtension
    static WireMockExtension wm1 = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().dynamicHttpsPort())
            .build();*/

/*

    static WireMockServer wireMockServer = new WireMockServer();
/*
    @BeforeAll
    public static void beforeAll() {

        WireMock.configureFor(8090);
        //WireMock.configureFor("custom-host", 9000, "/api-root-url");
        wireMockServer.start();
    }

    @AfterAll
    public static void afterAll() {

        wireMockServer.stop();
    }

    @AfterEach
    public void afterEach() {

        wireMockServer.resetAll();
    }*/

    @Autowired
    private JwtTokenClient jwtTokenClient;

    @Test
    public void getTokenTest(){

        UserMainDto userData = new UserMainDto("admin", "100");
        String token = jwtTokenClient.getToken(userData);
        Assertions.assertThat(token).contains(".");
    }

}
