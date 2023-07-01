package ru.clevertec.finalproj.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс содержащий данные для запроса JwtToken на сервисе авторизации
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMainDto {
    private String username;
    private String password;
}
