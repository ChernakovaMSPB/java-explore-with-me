package ru.practicum.main.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main.dto.CommentsDto;
import ru.practicum.main.dto.NewCommentsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentsService {

    CommentsDto createNewComment(Long userId, Long eventId, NewCommentsDto commentsDto);

    CommentsDto updateCommentByUser(Long userId, Long commentId, NewCommentsDto commentsDto);

    void deleteCommentByUser(Long userId, Long commentId);

    void deleteComment(Long commentId);

    List<CommentsDto> search(String text, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

}
