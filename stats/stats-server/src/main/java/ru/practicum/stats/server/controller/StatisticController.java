package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class StatisticController {
    private final StatisticService statisticService;
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam(name = "start") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
                                       @RequestParam(name = "end") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
                                       @RequestParam(name = "uris", required = false) List<String> uris,
                                       @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique) {
        return statisticService.getStats(start, end, uris, unique);

    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createHit(@RequestBody EndpointHitDto endpointHitDto) {
        return statisticService.postHit(endpointHitDto);
    }
}