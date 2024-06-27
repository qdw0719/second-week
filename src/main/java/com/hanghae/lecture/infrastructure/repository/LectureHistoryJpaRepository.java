package com.hanghae.lecture.infrastructure.repository;

import com.hanghae.lecture.infrastructure.entity.LectureHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureHistoryJpaRepository extends JpaRepository<LectureHistory, Long> {
}
