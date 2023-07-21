package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.exception.ValidateException;
import ru.practicum.server.mapper.StatisticMapper;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticMapper statisticMapper;
    private final StatisticRepository statisticRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris == null || uris.isEmpty()) {
            return Collections.emptyList();
        }
        if (end.isBefore(start)) {
            throw new ValidateException("Дата окончания не может быть раньше даты начала");
        }
        List<ViewStatsDto> stats;
        if (unique) {
            stats = statisticRepository.getUniqueStats(start, end, uris);
        } else {
            stats = statisticRepository.getStats(start, end, uris);
        }
        System.out.println(stats);
        return stats;
    }

    @Override
    @Transactional
    public EndpointHitDto postHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statisticMapper.toEndpointHit(endpointHitDto);
        EndpointHit postedHit = statisticRepository.save(endpointHit);
        return statisticMapper.toEndpointHitDto(postedHit);
    }

}
