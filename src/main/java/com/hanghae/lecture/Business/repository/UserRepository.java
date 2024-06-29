package com.hanghae.lecture.Business.repository;

import com.hanghae.lecture.infrastructure.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    List<User> saveAll(List<User> users);
    void flush();
    void deleteAll();
}
