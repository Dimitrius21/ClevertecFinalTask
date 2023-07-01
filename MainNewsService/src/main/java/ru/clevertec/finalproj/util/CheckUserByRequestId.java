package ru.clevertec.finalproj.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Component;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;
import ru.clevertec.finalproj.domain.entity.Comment;
import ru.clevertec.finalproj.domain.entity.News;
import ru.clevertec.finalproj.repository.CommentRepository;
import ru.clevertec.finalproj.repository.NewsRepository;

/**
 * Класс для проверки соответствия имени идентифицированного пользователя с имением пользователя в записи БД для данного id
 */
@Component
@AllArgsConstructor
public class CheckUserByRequestId implements CheckUserInRequest {

    @Autowired
    CommentRepository commentRepo;

    @Autowired
    NewsRepository newsRepo;

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
        long id = ParseRequest.getId(request);
        String usernameInNote = "";
        String entity = ParseRequest.getEntityName(request);
        switch (entity) {
            case "comment": {
                Comment comment = commentRepo.findById(id).orElseThrow(() -> new ResourceNotFountException("Resource with id=" + id + " not found"));
                usernameInNote = comment.getUsername();
                break;
            }
            case "news": {
                News news = newsRepo.findById(id).orElseThrow(() -> new ResourceNotFountException("Resource with id=" + id + " not found"));
                usernameInNote = news.getUsername();
                break;
            }
        }
        if (username.equals(usernameInNote)) {
            return confirmDecision;
        } else {
            return rejectDecision;
        }
    }
}
