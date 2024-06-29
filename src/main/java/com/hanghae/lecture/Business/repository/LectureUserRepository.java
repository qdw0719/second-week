package com.hanghae.lecture.Business.repository;

import com.hanghae.lecture.infrastructure.entity.LectureUser;

import java.util.List;
import java.util.Optional;

public interface LectureUserRepository {
    List<LectureUser> findAll();
    Optional<LectureUser> findByLectureIdAndUserId(Long lectureId, Long userId);
    LectureUser save(LectureUser lectureUser);
    long countByLectureId(Long lectureId);
    void deleteAll();
    Optional<LectureUser> findByUserId(Long userId);
}
