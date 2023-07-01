package ru.clevertec.AuthServer.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.AuthServer.domain.dto.UserDto;
import ru.clevertec.AuthServer.domain.dto.UserMainDto;
import ru.clevertec.AuthServer.domain.dto.UserRespDto;
import ru.clevertec.AuthServer.domain.entity.Role;
import ru.clevertec.AuthServer.repository.RoleRepository;
import ru.clevertec.AuthServer.repository.UserRepository;
import ru.clevertec.AuthServer.utils.JwtTokenUtil;
import ru.clevertec.AuthServer.utils.UserRoles;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {JwtTokenUtil.class})
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Spy
    private PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

    @Spy
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserRepository userRepo;

    @Mock
    private RoleRepository roleRepo;

    @InjectMocks
    AuthService authService;

    @Test
    void createJwtTokenTest() {
        String username = "admin";

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "100");
        UserDetails user = User.builder().username(username).authorities("ROLE_ADMIN").password("100").build();
        Authentication authResult = new UsernamePasswordAuthenticationToken(user, null);

        UserMainDto userDto = new UserMainDto("admin", "100");
        String jwtToken = authService.createJwtToken(authResult);

        String payloadBase64 = jwtToken.split("\\.")[1];
        String payload = new String(Base64.getDecoder().decode(payloadBase64));
        Assertions.assertThat(payload).contains("admin", "ROLE_ADMIN");
    }

    @Test
    void createUserTest() {

        UserDto userDto = new UserDto("Pavel", "321", "pavel@mail.com");
        Role role = new Role(2, "ROLE_SUBSCRIBER");

        ru.clevertec.AuthServer.domain.entity.User userForSave = new ru.clevertec.AuthServer.domain.entity.User(0,
                "Pavel",
                passwordEncoder.encode("321"),
                "pavel@mail.com",
                List.of(role));
        ru.clevertec.AuthServer.domain.entity.User userSaved = cloneUser(userForSave);
        userSaved.setId(10);
        UserRespDto expectedUser = UserRespDto.toUserRespDto(userSaved);


        when(roleRepo.findByRole("ROLE_SUBSCRIBER")).thenReturn(Optional.of(role));
        when(userRepo.save(userForSave)).thenReturn(userSaved);

        UserRespDto res = authService.createUser(userDto, UserRoles.ROLE_SUBSCRIBER);

        Assertions.assertThat(res).isEqualTo(expectedUser);
    }

    private ru.clevertec.AuthServer.domain.entity.User cloneUser(ru.clevertec.AuthServer.domain.entity.User user) {
        return new ru.clevertec.AuthServer.domain.entity.User(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRoles());
    }
}