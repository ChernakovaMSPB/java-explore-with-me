package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.*;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.ValidationException;
import ru.practicum.main.mappers.CommentsMapper;
import ru.practicum.main.mappers.EventMapper;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Comments;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;
import ru.practicum.main.model.enums.EventState;
import ru.practicum.main.model.enums.SortParam;
import ru.practicum.main.model.enums.StateAction;
import ru.practicum.main.model.enums.StateActionAdmin;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.CommentsRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.service.EventService;
import ru.practicum.main.service.EventSpecifications;
import ru.practicum.stats.client.StatisticClient;
import ru.practicum.stats.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventMapper mapper;
    private final EventRepository repository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StatisticClient statisticClient;
    private final CommentsMapper commentsMapper;
    private final CommentsRepository commentsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        Collection<Event> events;
        List<EventFullDto> eventsForAdmin = new ArrayList<>();
        List<CommentsDto> comments;
        Specification<Event> specification = EventSpecifications.getFilteredEventsForAdmin(users, states, categories, rangeStart, rangeEnd);
        Page<Event> eventPage = repository.findAll(specification, pageable);
        if (eventPage.isEmpty()) {
            throw new BadRequestException("Not events.");
        }
        events = eventPage.getContent();
        for (Event event : events) {
            comments = getComments(event);
            eventsForAdmin.add(mapper.toEventFullDto(event, comments));
        }
        return eventsForAdmin;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = findEventById(eventId);
        if (event.getPublishedOn() != null) {
            if (event.getPublishedOn().plusHours(1).isBefore(event.getEventDate())) {
                throw new ValidationException("The event does not meet the editing rules.");
            }
        }

        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory()).orElseThrow(() -> new NotFoundException("Category is not found"));
            event.setCategory(category);
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
                if (event.getState().equals(EventState.PUBLISHED)) {
                    throw new ValidationException("The event was already published.");
                }
                if (event.getState().equals(EventState.CANCELED)) {
                    throw new ValidationException("The event was cancelled.");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                event.setViews(0L);
            } else {
                if (event.getState().equals(EventState.PUBLISHED)) {
                    throw new ValidationException("The event was already published and can not be cancelled.");
                }
                event.setState(EventState.CANCELED);
            }
        }

        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Wrong event date.");
            }
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        mapper.update(updateEventAdminRequest, event);
        repository.save(event);
        return mapper.toEventFullDto(event);
    }

    @SneakyThrows
    @Override
    @Transactional(readOnly = true)
    public EventFullDto getPublishedEvent(Long id, String ip, String path) {
        saveHit(ip, path);

        Event event = repository.findEventById(id);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event not publich.");
        }
        List<CommentsDto> comments = getComments(event);
        event.setViews(event.getViews() + 1);
        return mapper.toEventFullDto(event, comments);
    }

    private List<CommentsDto> getComments(Event event) {
        List<Comments> comments = commentsRepository.findAllByEvent_Id(event.getId());
        return commentsMapper.toCommentsDtoCollection(comments);
    }

    private void saveHit(String ip, String path) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setIp(ip);
        endpointHitDto.setUri(path);
        endpointHitDto.setApp("main");
        endpointHitDto.setTimestamp(LocalDateTime.now());
        statisticClient.postHit(endpointHitDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getPublishedEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, SortParam sort, Pageable pageable, String ip, String path) {
        Collection<Event> events;
        List<EventShortDto> publishedEvents = new ArrayList<>();
        List<CommentsDto> comments;

        Specification<Event> specification = EventSpecifications.getFilteredEvents(text, categories, paid, rangeStart, rangeEnd, sort, EventState.PUBLISHED, onlyAvailable);
        Page<Event> eventPage = repository.findAll(specification, pageable);
        if (eventPage.isEmpty()) {
            throw new BadRequestException("EventPage empty");
        }
        events = eventPage.getContent();
        saveHit(ip, path);
        for (Event event : events) {
            event.setViews(event.getViews() + 1);
            comments = getComments(event);
            publishedEvents.add(mapper.toEventShortDto(event, comments));
        }
        return publishedEvents;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsOfUser(Long userId, Pageable pageable) {
        Collection<Event> events = repository.findAllByInitiator_Id(userId, pageable);
        List<EventShortDto> eventsOfUser = new ArrayList<>();
        List<CommentsDto> comments;
        for (Event event : events) {
            comments = getComments(event);
            eventsOfUser.add(mapper.toEventShortDto(event, comments));
        }
        return eventsOfUser;
    }

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getDescription() == null) {
            throw new BadRequestException("Field: eventDescription. Error: Description is null.");
        }

        Event event = mapper.toEvent(newEventDto);

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User is not found"));
        event.setInitiator(user);

        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Field: eventDate. Error: must contain a date that has not yet occurred.");
        }
        event.setCreatedOn(LocalDateTime.now());

        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new NotFoundException("Category is not found"));
        event.setCategory(category);

        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0L);

        Event addedEvent = repository.save(event);
        return mapper.toEventFullDto(addedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventOfUser(Long userId, Long eventId) {
        return mapper.toEventFullDto(repository.findEventByIdAndInitiator_Id(eventId, userId));
    }

    @Override
    @Transactional
    public EventFullDto updateEventOfUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User is not found"));
        Event event = findEventById(eventId);
        if (event.getInitiator().getId().equals(user.getId())) {
            if (updateEventUserRequest == null) {
                return mapper.toEventFullDto(event);
            }
            if (event.getPublishedOn() != null) {
                throw new ValidationException("Only pending or canceled events can be changed.");
            }
            if (updateEventUserRequest.getCategory() != null) {
                Category category = categoryRepository.findById(updateEventUserRequest.getCategory()).orElseThrow(() -> new NotFoundException("Category is not found"));
                event.setCategory(category);
            }

            if (updateEventUserRequest.getEventDate() != null) {
                if (event.getEventDate().minusHours(2).isAfter(LocalDateTime.now())) {
                    throw new BadRequestException("Only pending or canceled events can be changed.");
                }
                event.setEventDate(updateEventUserRequest.getEventDate());
            }

            if (updateEventUserRequest.getStateAction() != null && updateEventUserRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else {
                event.setState(EventState.CANCELED);
            }
            mapper.updateEventOfUser(updateEventUserRequest, event);
            repository.save(event);
        }
        return mapper.toEventFullDto(event);
    }

    @Transactional(readOnly = true)
    public Event findEventById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Event  is not found"));
    }
}