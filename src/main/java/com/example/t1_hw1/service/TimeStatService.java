package com.example.t1_hw1.service;

import com.example.t1_hw1.annotation.TrackTime;
import com.example.t1_hw1.domain.TimeStat;
import com.example.t1_hw1.model.response.TimeStatResponse;
import com.example.t1_hw1.repository.TimeStatRepository;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;

/**
 * Сервис для работы с временной статистикой
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TimeStatService {

    private final TimeStatRepository timeStatRepository;

    /**
     * Получить минимальное время ответа метода
     * Если имя метода пустое, то минимальное время ответа расчитывается среди всех методов
     *
     * @param methodName Имя метода
     * @return минимальное время ответа
     */
    @Nonnull
    @Transactional(readOnly = true)
    @TrackTime
    public TimeStatResponse findMin(@Nullable String methodName) {

        List<TimeStat> timeStats = methodName == null
                ? timeStatRepository.findAll()
                : timeStatRepository.selectByMethodName(methodName);

        return timeStats
                .stream()
                .min(Comparator.comparing(TimeStat::getDurationMills))
                .map(this::buildTimeStatResponse)
                .orElse(null);
    }

    /**
     * Получить максимальное время ответа метода
     * Если имя метода пустое, то максимальное время ответа расчитывается среди всех методов
     *
     * @param methodName Имя метода
     * @return максимальное время ответа
     */
    @Nonnull
    @Transactional(readOnly = true)
    @TrackTime
    public TimeStatResponse findMax(@Nullable String methodName) {

        List<TimeStat> timeStats = methodName == null
                ? timeStatRepository.findAll()
                : timeStatRepository.selectByMethodName(methodName);

        return timeStats
                .stream()
                .max(Comparator.comparing(TimeStat::getDurationMills))
                .map(this::buildTimeStatResponse)
                .orElse(null);
    }

    /**
     * Получить среднее время ответа метода
     * Если имя метода пустое, то среднее время ответа расчитывается среди всех методов
     *
     * @param methodName Имя метода
     * @return среднее время ответа
     */
    @Nonnull
    @Transactional(readOnly = true)
    @TrackTime
    public TimeStatResponse findAvg(@Nullable String methodName) {

        var response = new TimeStatResponse();

        List<TimeStat> timeStats = methodName == null
                ? timeStatRepository.findAll()
                : timeStatRepository.selectByMethodName(methodName);

        if (timeStats.isEmpty()) {
            return response;
        }

        var avgDurationTime = timeStats
                .stream()
                .mapToDouble(TimeStat::getDurationMills)
                .average()
                .orElse(0.0);

        ofNullable(methodName).map(response::setMethod);
        return response.setDuration(avgDurationTime);
    }

    @Nonnull
    private TimeStatResponse buildTimeStatResponse(@Nonnull TimeStat timeStat) {
        return new TimeStatResponse()
                .setMethod(timeStat.getMethod())
                .setDuration((double) timeStat.getDurationMills())
                .setIsAsync(timeStat.getIsAsync());
    }

    /*@Async
    public void saveTimeStat(@Nonnull TimeStat timeStat) {
         timeStatRepository.save(timeStat);
    }*/

    /**
     * Сохранить статистику выполнения метода
     *
     * @param timeStat Временная статистика выполнения метода
     * @return Временная статистика выполнения метода
     */
    @Nonnull
    @Async
    public CompletableFuture<TimeStat> saveTimeStat(TimeStat timeStat) {
        return CompletableFuture.completedFuture(timeStatRepository.save(timeStat));
    }

    /*@Async
    public CompletableFuture<TimeStat> calcExecution() throws InterruptedException {
        log.info(String.format("method execution time: %d", 100));
        //System.out.println("Looking up " + page);
        //TimeStat results = restTemplate.getForObject("http://graph.facebook.com/" + page, Page.class);
        Thread.sleep(1000L);
        return new AsyncResult<>(results);
    }*/

}
