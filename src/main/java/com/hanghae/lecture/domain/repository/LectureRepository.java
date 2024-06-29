package com.hanghae.lecture.domain.repository;

import com.hanghae.lecture.domain.model.Lecture;

import java.util.List;
import java.util.Optional;

public interface LectureRepository {
    List<Lecture> findAll();
    Optional<Lecture> findById(Long id);
    Lecture save(Lecture lecture);
    void deleteById(Long id);
    long count();
    void deleteAll();
    Optional<Lecture> findByIdWithPessimisticLock(Long lectureId);
}
