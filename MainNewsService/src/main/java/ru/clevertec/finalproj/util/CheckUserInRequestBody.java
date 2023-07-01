package ru.clevertec.finalproj.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Component;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.finalproj.domain.dto.CommentDto;
import ru.clevertec.finalproj.domain.dto.NewsShortDto;

import java.io.IOException;
import java.util.Objects;

/**
 * Класс для проверки соответствия имени идентифицированного пользователя с имением пользователя в теле запроса с передаваемым объектом
 */
@Component
@AllArgsConstructor
public class CheckUserInRequestBody implements CheckUserInRequest {

    @Autowired
    private ObjectMapper objectMapper;

    private final AuthorizationDecision confirmDecision = new AuthorizationDecision(true);
    private final AuthorizationDecision rejectDecision = new AuthorizationDecision(false);

    /**
     * Метод проверяет совпадение имени идентифицированного пользователя с имением пользователя в записи БД для id запроса
     *
     * @param username имя идентифицированного пользователя
     * @param request  - объект http запроса
     * @return - объект типа AuthorizationDecision с соответствующим решением соответствия
     */
    public AuthorizationDecision check(String username, HttpServletRequest request) {
        String body = null;
        try {
            body = new String(request.getInputStream().readAllBytes());
            String entity = ParseRequest.getEntityName(request);
            objectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
            String usernameInBody = switch (entity) {
                case "news" -> objectMapper.readValue(body, NewsShortDto.class).getUsername();
                case "comment" -> objectMapper.readValue(body, CommentDto.class).getUsername();
                default -> "";
            };
            if (Objects.isNull(usernameInBody)) {
                throw new RequestBodyIncorrectException("Body of request is incorrect");
            }
            if (username.equals(usernameInBody)) {
                return confirmDecision;
            } else {
                return rejectDecision;
            }
        } catch (JsonProcessingException ex) {
            throw new RequestBodyIncorrectException("Body of request can't be parsed");
        } catch (IOException e) {
            throw new RequestBodyIncorrectException("Error of body reading");
        }
    }
}