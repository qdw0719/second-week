package com.hanghae.lecture.infrastructure.repository;

import com.hanghae.lecture.domain.model.User;
import com.hanghae.lecture.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return userJpaRepository.saveAll(users);
    }

    @Override
    public void flush() {
        userJpaRepository.flush();
    }

    @Override
    public void deleteAll() {
        userJpaRepository.deleteAll();
    }
}
