package ru.clevertec.AuthServer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.AuthServer.domain.entity.User;

import java.util.Optional;

/**
 * Класс для доступа к базе данным для сущности User
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
