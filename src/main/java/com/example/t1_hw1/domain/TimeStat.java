package com.example.t1_hw1.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "time_stat")
public class TimeStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "started_at_mills", nullable = false)
    private Long startedAtMills;

    @Column(name = "ended_at_mills", nullable = false)
    private Long endedAtMills;

    @Column(name = "duration_mills", nullable = false)
    private Long durationMills;

    @Column(name = "is_async", nullable = false)
    private Boolean isAsync;

    @Override
    public String toString() {
        return "TimeStat{" +
                "id=" + id +
                ", method='" + method + '\'' +
                ", startedAtMills=" + startedAtMills +
                ", endedAtMills=" + endedAtMills +
                ", durationMills=" + durationMills +
                ", isAsync=" + isAsync +
                '}';
    }
}
