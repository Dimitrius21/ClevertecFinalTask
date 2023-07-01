package ru.clevertec.finalproj.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.finalproj.domain.entity.Comment;
import ru.clevertec.finalproj.domain.entity.News;

import java.time.LocalDateTime;

/**
 * Класс с данными для запросов к сущности Comment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    @NotBlank
    private String text;
    @NotBlank
    private String username;
    @Min(1)
    private long newsId;

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto(comment.getId(), comment.getTime(), comment.getText(), comment.getUsername(), comment.getNews().getId());
        return commentDto;
    }

    public static Comment toComment(CommentDto commentDto) {
        News news = new News();
        news.setId(commentDto.getNewsId());
        Comment comment = new Comment(commentDto.getId(), commentDto.getTime(), commentDto.getText(), commentDto.getUsername(),
                news);
        return comment;
    }

}
