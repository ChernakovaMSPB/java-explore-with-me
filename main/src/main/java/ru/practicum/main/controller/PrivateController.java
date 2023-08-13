package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.*;
import ru.practicum.main.service.CommentsService;
import ru.practicum.main.service.EventService;
import ru.practicum.main.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateController {
    private final EventService eventService;
    private final RequestService requestService;
    private final CommentsService commentsService;
    private static final String USER_EVENTS_PATH = "/{userId}/events";
    private static final String USER_EVENT_PATH = "/{userId}/events/{eventId}";
    private static final String USER_REQUESTS_PATH = "/{userId}/requests";
    private static final String USER_REQUEST_PATH = "/{userId}/events/{eventId}/requests";
    private static final String USER_COMMENT_PATH = "/{userId}/comments/{commentId}";

    @GetMapping(USER_EVENTS_PATH)
    public List<EventShortDto> getEventsOfUser(@PathVariable Long userId,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return eventService.getEventsOfUser(userId, PageRequest.of(from / size, size));
    }

    @PostMapping(USER_EVENTS_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping(USER_EVENT_PATH)
    public EventFullDto getEventOfUser(@PathVariable Long userId,
                                       @PathVariable Long eventId) {
        return eventService.getEventOfUser(userId, eventId);
    }

    @PostMapping(USER_REQUESTS_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable Long userId,
                                                           @RequestParam(name = "eventId") String eventId) {
        return requestService.addParticipationRequest(userId, Long.parseLong(eventId));
    }

    @PatchMapping(USER_EVENT_PATH)
    public EventFullDto updateEventOfUser(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("updateEventOfUser with updateEventUserRequest {}", updateEventUserRequest);
        return eventService.updateEventOfUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping(USER_REQUEST_PATH)
    public List<ParticipationRequestDto> getEventParticipants(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return requestService.getEventParticipants(userId, eventId);
    }

    @PatchMapping(USER_REQUEST_PATH)
    public EventRequestStatusUpdateResult changeRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        return requestService.changeRequestStatus(userId, eventId, statusUpdateRequest);
    }

    @GetMapping(USER_REQUESTS_PATH)
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        return requestService.getUserRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentsDto createNewComment(@PathVariable Long userId,
                                        @RequestParam(name = "eventId") String eventId,
                                        @RequestBody @Valid NewCommentsDto commentsDto) {
        return commentsService.createNewComment(userId, Long.parseLong(eventId), commentsDto);
    }

    @PatchMapping(USER_COMMENT_PATH)
    public CommentsDto updateCommentByUser(@PathVariable Long userId,
                                           @PathVariable Long commentId,
                                           @RequestBody NewCommentsDto commentsDto) {
        return commentsService.updateCommentByUser(userId, commentId, commentsDto);
    }

    @DeleteMapping(USER_COMMENT_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByUser(@PathVariable Long userId,
                                    @PathVariable Long commentId) {
        commentsService.deleteCommentByUser(userId, commentId);
    }
}
