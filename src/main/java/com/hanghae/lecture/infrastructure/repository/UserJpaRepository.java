package com.hanghae.lecture.infrastructure.repository;

import com.hanghae.lecture.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
}
