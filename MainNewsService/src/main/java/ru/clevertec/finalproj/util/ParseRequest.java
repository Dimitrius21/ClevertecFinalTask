package ru.clevertec.finalproj.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

/**
 * Класс содержащий вспомогательные методы для получения данных из http запроса
 */
public class ParseRequest {

    /**
     * Метод возвращает id из url полученного запроса
     * @param request объект запроса
     * @return - id
     */
    public static long getId(HttpServletRequest request) {
        String[] urlParts = request.getRequestURL().toString().split("/");
        String stringId = urlParts[urlParts.length - 1];
        long id = Long.parseLong(stringId);
        return id;
    }

    /**
     * Метод возвращает строку с названием роли необходимой для действия над сущностью указанной в url запроса
     * @param request объект запроса
     * @return - требуемая роль идентифицированного пользователя
     */
    public static String getCorrespondedRole(HttpServletRequest request) {
        String role = Entity.getRole(getEntityName(request));
        return role;
    }

    /**
     * Метод для получения названия сущности в url запроса
     * @param request объект запроса
     * @return имя сущности в приложении
     */
    public static String getEntityName(HttpServletRequest request) {
        String[] urlParts = request.getRequestURL().toString().split("/");
        int index = Arrays.asList(urlParts).indexOf("api");
        return urlParts[index + 1];
    }

    /**
     * Внутренний объект с соответствиями имен сущностей и требуемой ролью для действия над ней
     */
    enum Entity {
        comment("ROLE_SUBSCRIBER"),
        news("ROLE_JOURNALIST");

        String role;

        Entity(String role) {
            this.role = role;
        }

        public static String getRole(String entity) {
            return Entity.valueOf(entity).role;
        }
    }
}
