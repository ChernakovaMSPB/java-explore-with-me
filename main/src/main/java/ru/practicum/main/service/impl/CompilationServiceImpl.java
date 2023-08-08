package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationRequest;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mappers.CompilationMapper;
import ru.practicum.main.mappers.EventMapper;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.service.CompilationService;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationMapper mapper;
    private final EventMapper eventMapper;
    private final CompilationRepository repository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {

        if (newCompilationDto.getTitle() != null && newCompilationDto.getTitle().length() > 50) {
            throw new BadRequestException("Title not null");
        }
        Compilation compilation = mapper.toCompilation(newCompilationDto);

        return mapper.toCompilationDto(repository.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        findCompilationById(compId);
        repository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilationDto) {

        if (compilationDto != null && compilationDto.getTitle() != null && compilationDto.getTitle().length() > 50) {
            throw new BadRequestException("Title not null");
        }
        Compilation compilationToUpdate = findCompilationById(compId);

        mapper.update(compilationDto, compilationToUpdate);

        if (compilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(compilationDto.getEvents());
            compilationToUpdate.setEvents(new HashSet<>(events));
        }
        repository.save(compilationToUpdate);
        return mapper.toCompilationDto(compilationToUpdate);
    }

    @Transactional(readOnly = true)
    public Compilation findCompilationById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Compilation " + id + " is not found."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        return mapper.toCompilationDto(repository.findAllByPinnedIs(pinned, pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(Long compId) {
        return mapper.toCompilationDto(findCompilationById(compId));
    }
}