package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                       @RequestParam(name = "uris", required = false) List<String> uris,
                                       @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique) {
        return statisticService.getStats(start, end, uris, unique);

    }

    @PostMapping("/hit")
    public EndpointHitDto createHit(@RequestBody EndpointHitDto endpointHitDto) {
        return statisticService.postHit(endpointHitDto);
    }
}