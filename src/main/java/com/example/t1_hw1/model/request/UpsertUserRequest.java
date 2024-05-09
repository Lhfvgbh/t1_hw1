package com.example.t1_hw1.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Модель запроса на создание и обновление пользователя
 */
@Data
@Accessors(chain = true)
public class UpsertUserRequest {
    private Integer id;
    private String login;
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer age;
}