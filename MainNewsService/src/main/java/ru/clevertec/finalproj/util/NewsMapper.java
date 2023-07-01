package ru.clevertec.finalproj.util;

import org.mapstruct.Mapper;
import ru.clevertec.finalproj.domain.dto.NewsShortDto;
import ru.clevertec.finalproj.domain.entity.News;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsShortDto toNewsShortDto(News news);
}
