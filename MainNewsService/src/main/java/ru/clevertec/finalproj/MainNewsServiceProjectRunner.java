package ru.clevertec.finalproj;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Clevertec. Final Project", version = "1.0", description = "Documentation APIs v1.0"))
public class MainNewsServiceProjectRunner {
    public static void main(String[] args) {
        SpringApplication.run(MainNewsServiceProjectRunner.class);
    }
}
