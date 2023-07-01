package ru.clevertec.finalproj.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.finalproj.domain.entity.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс с возвращаемыми данными после запросов к сущности News
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsFullDto {
    private long id;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private String title;
    private String text;
    private String username;
    private List<CommentDto> comments = new ArrayList<>();

    public static NewsFullDto toNewsFullDto(News news) {
        List<CommentDto> commentDtoList = news.getComments().stream().map(CommentDto::toCommentDto).toList();
        return new NewsFullDto(news.getId(), news.getTime(), news.getTitle(), news.getText(), news.getUsername(), commentDtoList);
    }
}
