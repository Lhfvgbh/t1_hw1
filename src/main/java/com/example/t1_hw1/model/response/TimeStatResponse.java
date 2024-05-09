package com.example.t1_hw1.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Модель ответа на запрос временной статистики
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeStatResponse {
    private String method;
    private Double duration;
    private Boolean isAsync;
}
