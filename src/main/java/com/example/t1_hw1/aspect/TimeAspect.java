package com.example.t1_hw1.aspect;

import com.example.t1_hw1.domain.TimeStat;
import com.example.t1_hw1.service.TimeStatService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Аспект обработки времени выполнения синхронных методов
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class TimeAspect {

    private final TimeStatService timeStatService;

    /**
     * Поиск методов класса {@link com.example.t1_hw1.service.UserService},
     * аннотированных {@link com.example.t1_hw1.annotation.TrackTime}
     */
    @Pointcut("execution(@com.example.t1_hw1.annotation.TrackTime " +
            "public * com.example.t1_hw1.service.UserService.* (..)) ")
    public void syncTimePointcut() {
    }

    /**
     * Обработка синхронных методов, подходящих под pointcut
     */
    @Around("syncTimePointcut()")
    @Nullable
    public Object syncTimeStat(ProceedingJoinPoint joinPoint) {
        Object result = null;
        var startTime = System.currentTimeMillis();
        log.info("syncTimeStart: " + startTime);
        var timeStat = new TimeStat()
                .setMethod(joinPoint.getSignature().toShortString())
                .setIsAsync(false)
                .setStartedAtMills(startTime);
        try {
            log.info("Синхронный запуск в TimeAspect");
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Ошибка TimeAspect", e);
        } finally {
            var endTime = System.currentTimeMillis();
            log.info("syncTimeEnd: " + endTime);
            var duration = endTime - startTime;
            log.info("syncTimeDuration in ms: " + duration);
            timeStat = timeStat
                    .setEndedAtMills(endTime)
                    .setDurationMills(duration);
        }
        timeStatService.saveTimeStat(timeStat);
        return result;
    }
}
