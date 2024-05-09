package com.example.t1_hw1.controller;

import com.example.t1_hw1.model.request.UpsertUserRequest;
import com.example.t1_hw1.model.response.UserResponse;
import com.example.t1_hw1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Контроллер для работы с сущностями Пользователь
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Получить список всех пользователей
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    /**
     * Получить пользователя по id
     */
    @GetMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public UserResponse findById(@PathVariable Integer userId) {
        return userService.findById(userId);
    }

    /**
     * Создать пользователя
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public CompletableFuture<UserResponse> create(@RequestBody UpsertUserRequest request) {
        return userService.create(request);
    }

    /**
     * Обновить пользователя по id
     */
    @PatchMapping(value = "/{userId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public CompletableFuture<UserResponse> update(@PathVariable Integer userId, @RequestBody UpsertUserRequest request) {
        return userService.update(userId, request);
    }

    /**
     * Удалить пользователя по id
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Integer userId) {
        userService.delete(userId);
    }
}