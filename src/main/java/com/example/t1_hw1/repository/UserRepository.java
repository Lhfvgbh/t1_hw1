package com.example.t1_hw1.repository;

import com.example.t1_hw1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с таблицей `users`
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}