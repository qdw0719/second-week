package com.hanghae.lecture.Business.repository;

import com.hanghae.lecture.infrastructure.entity.LectureHistory;

import java.util.List;

public interface LectureHistoryRepository {
    List<LectureHistory> findAll();
    LectureHistory save(LectureHistory history);
}
