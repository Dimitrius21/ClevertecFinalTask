package ru.clevertec.AuthServer.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.AuthServer.domain.dto.UserDto;
import ru.clevertec.AuthServer.domain.dto.UserRespDto;
import ru.clevertec.AuthServer.domain.entity.Role;
import ru.clevertec.AuthServer.domain.entity.User;
import ru.clevertec.AuthServer.repository.RoleRepository;
import ru.clevertec.AuthServer.repository.UserRepository;
import ru.clevertec.AuthServer.utils.JwtTokenUtil;
import ru.clevertec.AuthServer.utils.UserRoles;

import java.util.List;

/**
 * Класс реализует слой Service
 */
@Service
@Transactional
@AllArgsConstructor
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    /**
     * Метод на основе данных идентифицированного пользователя возвращает JwtToken
     *
     * @param token - Authentication token c данными пользователя - имя и пароль
     * @return сформированный JwtToken
     */
    public String createJwtToken(Authentication token) {
        UserDetails userDetails;
        Object principal = token.getPrincipal();
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        } else {
            userDetails = new org.springframework.security.core.userdetails.User(token.getName(), "", token.getAuthorities());
        }
        String jwtToken = jwtTokenUtil.jwtTokenCreator(userDetails);
        return jwtToken;
    }

    /**
     * Метод сохраняет нового пользователя в системе
     *
     * @param userDto   - данные пользователя - имя, пароль и email пользователя
     * @param userRoles - роль пользователя в системе, определяющая возможные действия
     * @return - объект с данными созданного пользователя
     */
    public UserRespDto createUser(UserDto userDto, UserRoles userRoles) {
        Role role = roleRepo.findByRole(userRoles.name()).get();
        User user = new User(0, userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), userDto.getEmail(), List.of(role));
        user = userRepo.save(user);
//        user.setPassword(null);
        return UserRespDto.toUserRespDto(user);
    }

}
