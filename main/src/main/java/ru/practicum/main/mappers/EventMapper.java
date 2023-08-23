package ru.practicum.main.mappers;

import org.mapstruct.*;
import ru.practicum.main.dto.*;
import ru.practicum.main.model.Event;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(source = "category", target = "category.id")
    Event toEvent(NewEventDto newEventDto);

    @Mapping(source = "paid", target = "paid", defaultValue = "false")
    @Mapping(source = "requestModeration", target = "requestModeration", defaultValue = "true")
    @Mapping(target = "participantLimit", defaultValue = "0L")
    EventFullDto toEventFullDto(Event event);

    EventShortDto toEventShortDto(Event event);

    @Mapping(target = "id", source = "event.id")
    EventFullDto toEventFullDto(Event event, List<CommentsDto> comments);

    @Mapping(target = "id", source = "event.id")
    EventShortDto toEventShortDto(Event event, List<CommentsDto> comments);

    @Mapping(target = "annotation", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "location", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "paid", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "requestModeration", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "participantLimit", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "eventDate", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "category", ignore = true)
    void update(UpdateEventAdminRequest updateEventAdminRequest, @MappingTarget Event eventToUpdate);

    @Mapping(target = "annotation", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "location", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "paid", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "requestModeration", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "participantLimit", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "eventDate", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEventOfUser(UpdateEventUserRequest updateEventUserRequest, @MappingTarget Event eventToUpdate);
}
