package ru.clevertec.finalproj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.finalproj.domain.entity.News;

/**
 * Класс определяющий доступ к БД для сущности News
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}
