package ru.practicum.main.mappers;

import org.mapstruct.*;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationRequest;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompilationMapper {


    @Mapping(target = "pinned", defaultValue = "false")
    @Mapping(source = "events", target = "events")
    CompilationDto toCompilationDto(Compilation compilation);


    @Mapping(target = "events", qualifiedByName = "mapEvent")
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    default @Named("mapEvent")
    Set<Event> map(List<Long> value) {
        Set<Event> eventSet = new HashSet<>();
        if (value != null && !value.isEmpty()) {
            value.forEach(eventId -> {
                Event event = new Event();
                event.setId(eventId);
                eventSet.add(event);
            });

        }
        return eventSet;
    }

    ;

    @Mapping(target = "pinned", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "events", ignore = true)
    void update(UpdateCompilationRequest updateCompilationRequest, @MappingTarget Compilation compilationToUpdate);

    List<CompilationDto> toCompilationDto(List<Compilation> compilations);
}
