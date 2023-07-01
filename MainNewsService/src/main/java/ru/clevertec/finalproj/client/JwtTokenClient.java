package ru.clevertec.finalproj.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.finalproj.domain.dto.UserMainDto;

/**
 * Интерфейс описывающий создание Spring feign client для запросов в сервис идентификации
 */
@FeignClient(name = "${feign.name}", url = "${feign.url}")
public interface JwtTokenClient {
    @PostMapping("/auth/jwt")
    public String getToken(@RequestBody UserMainDto userData);
}


