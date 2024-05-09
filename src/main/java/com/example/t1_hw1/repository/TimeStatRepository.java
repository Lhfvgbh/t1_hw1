package com.example.t1_hw1.repository;

import com.example.t1_hw1.domain.TimeStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Репозиторий для работы с таблицей `time_stat`
 */
public interface TimeStatRepository extends JpaRepository<TimeStat, Integer> {

    @Query(value = "SELECT * FROM time_stat AS ts WHERE ts.method = :methodName", nativeQuery = true)
    List<TimeStat> selectByMethodName(@Param("methodName") String methodName);
}
