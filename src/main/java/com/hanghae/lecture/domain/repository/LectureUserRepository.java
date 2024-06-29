package com.hanghae.lecture.domain.repository;

import com.hanghae.lecture.domain.model.LectureUser;

import java.util.List;
import java.util.Optional;

public interface LectureUserRepository {
    List<LectureUser> findAll();
    Optional<LectureUser> findByLectureIdAndUserId(Long lectureId, Long userId);
    LectureUser save(LectureUser lectureUser);
    long countByLectureId(Long lectureId);
    void deleteAll();
}
