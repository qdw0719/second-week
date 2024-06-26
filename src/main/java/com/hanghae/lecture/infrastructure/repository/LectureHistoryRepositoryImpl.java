package com.hanghae.lecture.infrastructure.repository;

import com.hanghae.lecture.infrastructure.entity.LectureHistory;
import com.hanghae.lecture.Business.repository.LectureHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LectureHistoryRepositoryImpl implements LectureHistoryRepository {

    private final LectureHistoryJpaRepository lectureHistoryJpaRepository;

    @Override
    public List<LectureHistory> findAll() {
        return lectureHistoryJpaRepository.findAll();
    }

    @Override
    public LectureHistory save(LectureHistory history) {
        return lectureHistoryJpaRepository.save(history);
    }
}
