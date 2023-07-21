package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("select new ru.practicum.dto.ViewStatsDto(e.app, e.uri, count(distinct e.ip)) " +
            " from EndpointHit e " +
            "where e.timestamp >= ?1 " +
            "and e.timestamp <= ?2 " +
            "and e.uri in ?3  " +
            "group by e.app, e.uri " +
            "order by count (distinct e.ip) DESC")
    List<ViewStatsDto> getUniqueStats(LocalDateTime from, LocalDateTime to, List<String> uris);


    @Query("select new ru.practicum.dto.ViewStatsDto(e.app, e.uri, count(e.ip)) " +
            " from EndpointHit e " +
            "where e.timestamp >= ?1 " +
            "and e.timestamp <= ?2 " +
            "and e.uri in ?3  " +
            "group by e.app, e.uri " +
            "order by count(e.ip) DESC")
    List<ViewStatsDto> getStats(LocalDateTime from, LocalDateTime to, List<String> uris);
}
