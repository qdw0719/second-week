package com.hanghae.lecture.Business.repository;

import com.hanghae.lecture.infrastructure.entity.Lecture;

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
    Optional<Lecture> findFirstByTitle(String title);
    Iterable<Lecture> saveAll(List<Lecture> lectures);
}
