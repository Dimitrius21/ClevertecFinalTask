package ru.clevertec.AuthServer.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.AuthServer.domain.entity.Role;
import ru.clevertec.AuthServer.domain.entity.User;
import ru.clevertec.AuthServer.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepo;

    @InjectMocks
    UserService userService;

    @Test
    void loadUserByUsernameTest() {
        String username = "admin";
        Role role = new Role(1, "ROLE_ADMIN");
        User user = new User(1, username, "100", "admin@mail.com", List.of(role));
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        UserDetails userExpected = new org.springframework.security.core.userdetails.User(username,
                "100", List.of(new SimpleGrantedAuthority(role.getRole())));

        UserDetails res = userService.loadUserByUsername(username);

        Assertions.assertThat(res).isEqualTo(userExpected);
    }
}