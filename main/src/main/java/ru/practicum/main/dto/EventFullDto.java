package ru.practicum.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.model.enums.EventState;
import ru.practicum.main.model.Location;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    LocalDateTime createdOn;
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    UserShortDto initiator;
    Location location;
    Boolean paid;
    Long participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    EventState state;
    String title;
    Long views;
    List<CommentsDto> comments;
}

