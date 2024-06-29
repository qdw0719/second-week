package com.hanghae.lecture.domain.repository;

import com.hanghae.lecture.domain.model.LectureHistory;

import java.util.List;

public interface LectureHistoryRepository {
    List<LectureHistory> findAll();
    LectureHistory save(LectureHistory history);
}
