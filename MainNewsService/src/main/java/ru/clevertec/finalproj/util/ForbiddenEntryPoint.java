package ru.clevertec.finalproj.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import ru.clevertec.exceptionhandler.domain.ErrorIfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Класс реализующий интерфейс AuthenticationEntryPoint отправляющего сообщение об ошибке при неудачной авторизации
 */
public class ForbiddenEntryPoint implements AuthenticationEntryPoint {

    private static ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2)
            throws IOException {

        ErrorIfo error = new ErrorIfo(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), arg2.getMessage(), null);
        String msg = mapper.writeValueAsString(error);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        PrintWriter pw = response.getWriter();
        pw.println(msg);
    }

}
