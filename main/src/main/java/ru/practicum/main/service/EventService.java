package ru.practicum.main.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main.model.enums.EventState;
import ru.practicum.main.model.enums.SortParam;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.dto.NewEventDto;
import ru.practicum.main.dto.UpdateEventAdminRequest;
import ru.practicum.main.dto.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    EventFullDto getPublishedEvent(Long id, String ip, String path);

    List<EventShortDto> getPublishedEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, SortParam sort, Pageable pageable, String ip, String path);

    List<EventShortDto> getEventsOfUser(Long userId, Pageable pageable);

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventOfUser(Long userId, Long eventId);

    EventFullDto updateEventOfUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

}

