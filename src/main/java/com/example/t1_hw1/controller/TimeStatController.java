package com.example.t1_hw1.controller;

import com.example.t1_hw1.model.request.TimeStatRequest;
import com.example.t1_hw1.model.response.TimeStatResponse;
import com.example.t1_hw1.service.TimeStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Контроллер для работы со временной статистикой
 */
@RestController
@RequestMapping("/api/v1/time-stat")
@RequiredArgsConstructor
public class TimeStatController {

    private final TimeStatService timeStatService;

    /**
     * Получаем минимальное время выполнения метода
     */
    @GetMapping(value = "/min", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public TimeStatResponse findMinTime(@RequestBody TimeStatRequest request) {
        return timeStatService.findMin(request.getMethod());
    }

    /**
     * Получаем максимальное время выполнения метода
     */
    @GetMapping(value = "/max", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public TimeStatResponse findMaxTime(@RequestBody TimeStatRequest request) {
        return timeStatService.findMax(request.getMethod());
    }

    /**
     * Получаем среднее время выполнения метода
     */
    @GetMapping(value = "/avg", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public TimeStatResponse findAvgTime(@RequestBody TimeStatRequest request) {
        return timeStatService.findAvg(request.getMethod());
    }
}
