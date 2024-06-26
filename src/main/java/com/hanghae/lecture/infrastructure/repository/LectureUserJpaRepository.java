package com.hanghae.lecture.infrastructure.repository;

import com.hanghae.lecture.infrastructure.entity.LectureUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureUserJpaRepository extends JpaRepository<LectureUser, Long> {
    List<LectureUser> findByLectureId(Long lectureId);
    Optional<LectureUser> findByLectureIdAndUserId(Long lectureId, Long userId);
    long countByLectureId(Long lectureId);
    Optional<LectureUser> findByUserId(Long userId);
}
