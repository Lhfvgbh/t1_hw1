package com.example.t1_hw1.aspect;

import com.example.t1_hw1.domain.TimeStat;
import com.example.t1_hw1.service.TimeStatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Аспект обработки времени выполнения асинхронных методов
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class TimeAsyncAspect {

    private final TimeStatService timeStatService;

    /**
     * Поиск методов класса {@link com.example.t1_hw1.service.UserService},
     * аннотированных {@link com.example.t1_hw1.annotation.TrackAsyncTime}
     */
    @Pointcut("execution(@com.example.t1_hw1.annotation.TrackAsyncTime " +
            "public * com.example.t1_hw1.service.UserService.* (..)) ")
    public void asyncTimePointcut() {
    }

    /**
     * Обработка асинхронных методов, подходящих под pointcut
     */
    @Around("asyncTimePointcut()")
    public Object asyncTimeStat(ProceedingJoinPoint joinPoint) {
        Object result = null;
        var startTime = System.currentTimeMillis();
        log.info("asyncTimeStart: " + startTime);
        var timeStat = new TimeStat()
                .setMethod(joinPoint.getSignature().toShortString())
                .setIsAsync(true)
                .setStartedAtMills(startTime);
        try {
            log.info("Асинхронный запуск в TimeAsyncAspect");
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Ошибка TimeAsyncAspect", e);
        } finally {
            var endTime = System.currentTimeMillis();
            log.info("asyncTimeEnd: " + endTime);
            var duration = endTime - startTime;
            log.info("asyncTimeDuration in ms: " + duration);
            timeStat = timeStat
                    .setEndedAtMills(endTime)
                    .setDurationMills(duration);
        }
        timeStatService.saveTimeStat(timeStat);
        return result;
    }


    /*@Around("asyncTimePointcut()")
    public Object asyncTimeStat2(ProceedingJoinPoint joinPoint) {
        var startTime = System.currentTimeMillis();
        return CompletableFuture.runAsync(() -> {
            log.info("asyncTimeStart: " + System.currentTimeMillis());
            try {
                log.info("Асинхронный запуск в TimeAsyncAspect");
                joinPoint.proceed();
            } catch (Throwable e) {
                log.error("Ошибка TimeAsyncAspect", e);
            } finally {
                log.info("asyncTimeEnd: " + System.currentTimeMillis());
                log.info("asyncTimeDuration: " + System.currentTimeMillis());
            }
        });
    }*/
}
