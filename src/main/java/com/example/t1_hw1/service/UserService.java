package com.example.t1_hw1.service;

import com.example.t1_hw1.annotation.TrackAsyncTime;
import com.example.t1_hw1.annotation.TrackTime;
import com.example.t1_hw1.domain.User;
import com.example.t1_hw1.model.request.UpsertUserRequest;
import com.example.t1_hw1.model.response.UserResponse;
import com.example.t1_hw1.repository.UserRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;

/**
 * Сервис для работы с пользователями
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Получить список пользователей
     *
     * @return список пользователей
     */
    @Nonnull
    @Transactional(readOnly = true)
    @TrackTime
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::buildUserResponse)
                .toList();
    }

    /**
     * Получить пользователя по идентификатору
     *
     * @param userId Идентификатор пользователеля
     * @return пользователь
     */
    @Nonnull
    @Transactional(readOnly = true)
    @TrackTime
    public UserResponse findById(@Nonnull Integer userId) {
        return userRepository.findById(userId)
                .map(this::buildUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User " + userId + " is not found"));
    }

    /**
     * Сохранить пользователя
     *
     * @param userData Данные пользователя для обновления
     * @return Пользователь
     */
    @Nonnull
    @Transactional
    @Async
    @TrackAsyncTime
    public CompletableFuture<UserResponse> create(@Nonnull UpsertUserRequest userData) {
        var user = new User()
                .setLogin(userData.getLogin())
                .setAge(userData.getAge())
                .setFirstName(userData.getFirstName())
                .setMiddleName(userData.getMiddleName())
                .setLastName(userData.getLastName());

        var createdUser = buildUserResponse(userRepository.save(user));

        return CompletableFuture.completedFuture(createdUser);
    }

    /**
     * Обновить пользователя
     *
     * @param userData Данные пользователя для обновления
     * @return Пользователь
     */
    @Nonnull
    @Transactional
    @Async
    @TrackAsyncTime
    public CompletableFuture<UserResponse> update(@Nonnull Integer userId, @Nonnull UpsertUserRequest userData) {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            log.warn("update: User not found with userId = " + userId);
            return CompletableFuture.completedFuture(new UserResponse());
        }
        var user = userOpt.get();
        ofNullable(userData.getLogin()).map(user::setLogin);
        ofNullable(userData.getFirstName()).map(user::setFirstName);
        ofNullable(userData.getMiddleName()).map(user::setMiddleName);
        ofNullable(userData.getLastName()).map(user::setLastName);
        ofNullable(userData.getAge()).map(user::setAge);

        var updatedUser = buildUserResponse(userRepository.save(user));
        return CompletableFuture.completedFuture(updatedUser);
    }

    /**
     * Удалить пользователя по идентификатору
     *
     * @param userId Идентификатор пользователеля
     */
    @Transactional
    @Async
    @TrackAsyncTime
    public void delete(@Nonnull Integer userId) {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            log.warn("delete: User not found with userId = " + userId);
            return;
        }
        userRepository.deleteById(userId);
    }

    @Nonnull
    private UserResponse buildUserResponse(@Nonnull User user) {
        return new UserResponse()
                .setId(user.getId())
                .setLogin(user.getLogin())
                .setAge(user.getAge())
                .setFirstName(user.getFirstName())
                .setMiddleName(user.getMiddleName())
                .setLastName(user.getLastName());
    }

}