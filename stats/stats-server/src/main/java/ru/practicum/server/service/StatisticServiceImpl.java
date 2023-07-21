package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.mapper.StatisticMapper;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticMapper statisticMapper;
    private final StatisticRepository statisticRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStatsDto> result;
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                result = statisticRepository.findAllUniqueWhenUriIsEmpty(start, end);
            } else {
                result = statisticRepository.findAllUniqueWhenUriIsNotEmpty(start, end, uris);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                result = statisticRepository.findAllWhenUriIsEmpty(start, end);
            } else {
                result = statisticRepository.findAllWhenStarEndUris(start, end, uris);
            }
        }
        return result.stream().collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EndpointHitDto postHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statisticMapper.toEndpointHit(endpointHitDto);
        EndpointHit postedHit = statisticRepository.save(endpointHit);
        return statisticMapper.toEndpointHitDto(postedHit);
    }

}
