package ru.clevertec.finalproj.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.clevertec.finalproj.domain.dto.CommentDto;
import ru.clevertec.finalproj.domain.entity.Comment;
import ru.clevertec.finalproj.domain.entity.News;
import ru.clevertec.finalproj.repository.CommentRepository;
import ru.clevertec.finalproj.repository.NewsRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    CommentService commentService;

    @Test
    void getByIdTest() {
        long id = 1L;

        Comment comment = getComment(id);
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        CommentDto commentDto = CommentDto.toCommentDto(comment);

        CommentDto result = commentService.getById(id);

        Assertions.assertThat(result).isEqualTo(commentDto);
    }

    @Test
    void searchCommentWithTest() {
        PageRequest pageable = PageRequest.of(0, 3);
        long id = 1;

        String field1 = "username";
        String field2 = "text";
        String val1 = "Alice";
        String val2 = "Comment";
        Map<String, String> params = Map.of(field1, val1, field2, val2);

        Comment comment = Comment.builder().id(0).text(val2).username(val1).build();
        News news = new News();
        news.setId(1);
        Comment commentFound = Comment.builder().id(id).text(val2).username(val1).news(news).build();

        ExampleMatcher commentExampleMatcher = ExampleMatcher.matchingAll().withIgnoreNullValues();
        commentExampleMatcher = commentExampleMatcher.withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        commentExampleMatcher = commentExampleMatcher.withMatcher("text", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        commentExampleMatcher = commentExampleMatcher.withIgnorePaths("id");
        Example<Comment> commentExample = Example.of(comment, commentExampleMatcher);

        Page<Comment> pageOfComments = new PageImpl<>(List.of(commentFound));

        when(commentRepository.findAll(commentExample, pageable)).thenReturn(pageOfComments);

        List<CommentDto> result = commentService.searchCommentWith(params, pageable);

        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(id);
    }

    @Test
    void createTest() {
        Comment commentInRequest = getComment(0);
        News news = commentInRequest.getNews();
        long newsId = news.getId();
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));

        CommentDto commentInRequestDto = CommentDto.toCommentDto(commentInRequest);
        Comment commentAfterSave = getComment(1);
        when(commentRepository.save(any(Comment.class))).thenReturn(commentAfterSave);

        CommentDto commentDto = CommentDto.toCommentDto(commentAfterSave);

        CommentDto result = commentService.create(commentInRequestDto);

        Assertions.assertThat(result).isEqualTo(commentDto);
    }

    @Test
    void updateTest() {
        long id = 1;
        Comment commentInDb = getComment(id);
        when(commentRepository.findById(id)).thenReturn(Optional.of(commentInDb));

        Comment commentInRequest = getComment(id);
        commentInRequest.setText("commentNew");
        CommentDto commentDtoInRequest = CommentDto.toCommentDto(commentInRequest);

        Comment commentSaved = getComment(id);
        commentSaved.setTime(commentInDb.getTime());
        commentSaved.setText(commentInRequest.getText());
        when(commentRepository.save(commentSaved)).thenReturn(commentSaved);
        CommentDto commentDto = CommentDto.toCommentDto(commentSaved);

        CommentDto result = commentService.update(commentDtoInRequest);

        Assertions.assertThat(result).isEqualTo(commentDto);
    }

    @Test
    void deleteById() {
        long id = 1000L;

        long result = commentService.deleteById(id);

        verify(commentRepository).deleteById(id);
        Assertions.assertThat(result).isEqualTo(id);
    }

    private Comment getComment(long id){
        News news = new News();
        news.setId(1);
        Comment comment = Comment.builder().id(id).text("Comment").username("user").time(LocalDateTime.now()).news(news).build();
        return comment;
    }
}