package com.example.t1_hw1.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Модель запроса временной статистики
 */
@Data
@Accessors(chain = true)
public class TimeStatRequest {
    private String method;
}
