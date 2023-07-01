package ru.clevertec.AuthServer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.clevertec.AuthServer.domain.entity.User;

/**
 * Dto содержащее информацию о user после запроса на его создание
 */
@Data
@AllArgsConstructor
public class UserRespDto {
    private long id;
    private String username;
    private String email;

    public static UserRespDto toUserRespDto(User user){
        return new UserRespDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
