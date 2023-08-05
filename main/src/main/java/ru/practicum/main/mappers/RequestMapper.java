package ru.practicum.main.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.model.Request;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RequestMapper {
    ParticipationRequestDto toRequestDto(Request request);

    List<ParticipationRequestDto> toRequestDto(List<Request> request);
}
