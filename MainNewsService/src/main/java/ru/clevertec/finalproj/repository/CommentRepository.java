package ru.clevertec.finalproj.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.finalproj.domain.entity.Comment;

import java.util.List;

/**
 * Класс определяющий доступ к БД для сущности Comment
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    public List<Comment> findByNewsId(long newsId, Pageable pageable);
}
