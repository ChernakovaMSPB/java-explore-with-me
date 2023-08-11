package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.CommentsDto;
import ru.practicum.main.dto.NewCommentsDto;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.ValidationException;
import ru.practicum.main.mappers.CommentsMapper;
import ru.practicum.main.model.Comments;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;
import ru.practicum.main.model.enums.EventState;
import ru.practicum.main.repository.CommentsRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.service.CommentsService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository repository;
    private final CommentsMapper mapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentsDto createNewComment(Long userId, Long eventId, NewCommentsDto commentsDto) {
        if (commentsDto.getText().isBlank() || commentsDto.getText().isEmpty()) {
            throw new ValidationException("Comment can not be blank");
        }
        Event event = null;
        if (event != null) {
            event = eventRepository.findEventByIdAndAndState(eventId, EventState.PENDING);
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User is not found. Only registered user can add comment"));
        Comments comments = new Comments();
        comments.setCreated(LocalDateTime.now());
        comments.setEvent(event);
        comments.setAuthor(user);
        comments.setText(commentsDto.getText());
        repository.save(comments);
        return mapper.toCommentsDto(comments);
    }

    @Override
    @Transactional
    public void deleteCommentByUser(Long userId, Long commentId) {
        Comments comments = repository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment is not found"));
        if (!comments.getAuthor().getId().equals(userId)) {
            throw new ValidationException("Only author of the comment can delete it");
        }
        repository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentsDto updateCommentByUser(Long userId, Long commentId, NewCommentsDto commentsDto) {
        Comments comments = repository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment is not found"));
        if (!comments.getAuthor().getId().equals(userId)) {
            throw new ValidationException("Only author of the comment can change it");
        }
        if (commentsDto.getText().isBlank()) {
            throw new ValidationException("Comment can not be blank");
        }
        comments.setCreated(LocalDateTime.now());
        comments.setText(commentsDto.getText());
        repository.save(comments);
        return mapper.toCommentsDto(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsDto> search(String text, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        Collection<Comments> commentsSearch = repository.search(text, rangeStart, rangeEnd, pageable);
        return mapper.toCommentsDtoCollection(commentsSearch);
    }

    @Override
    @Transactional
    public void deleteComment(Long comentId) {
        repository.findById(comentId).orElseThrow(() -> new NotFoundException("Comment is not found"));
        repository.deleteById(comentId);
    }
}
