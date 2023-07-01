package ru.clevertec.AuthServer.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Класс для отправки сообщение об ошибке при аутентификации пользователя
 */
public class ErrorSender {

    public static void sendError(HttpServletResponse response, HttpStatus status, String msg)
            throws IOException {
        response.setContentType("text/plain");
        response.setStatus(status.value());
        PrintWriter wr = response.getWriter();
        wr.println(msg);
    }
}
