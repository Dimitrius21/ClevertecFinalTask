package ru.clevertec.AuthServer.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.AuthServer.domain.entity.Role;


import java.util.Optional;

/**
 * Класс для доступа к базе данным для сущности Role
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    public Optional<Role> findByRole(String role);
}
