package com.hanghae.lecture.infrastructure.repository;

import com.hanghae.lecture.infrastructure.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

import java.util.Optional;

@Repository
public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Lecture a WHERE a.id = :id")
    Optional<Lecture> findByIdWithPessimisticLock(@Param("id") Long id);
    Optional<Lecture> findFirstByTitle(String title);
}
