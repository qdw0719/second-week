package com.hanghae.lecture.infrastructure.repository;

import com.hanghae.lecture.infrastructure.entity.Lecture;
import com.hanghae.lecture.Business.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;

    @Override
    public List<Lecture> findAll() {
        return lectureJpaRepository.findAll();
    }

    @Override
    public Optional<Lecture> findById(Long id) {
        return lectureJpaRepository.findById(id);
    }

    @Override
    public Lecture save(Lecture lecture) {
        return lectureJpaRepository.save(lecture);
    }

    @Override
    public void deleteById(Long id) {
        lectureJpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        return lectureJpaRepository.count();
    }

    @Override
    public void deleteAll() {
        lectureJpaRepository.deleteAll();
    }

    @Override
    public Optional<Lecture> findByIdWithPessimisticLock(Long id) {
        return lectureJpaRepository.findByIdWithPessimisticLock(id);
    }

    @Override
    public Optional<Lecture> findFirstByTitle(String title) {
        return lectureJpaRepository.findFirstByTitle(title);
    }

    @Override
    public Iterable<Lecture> saveAll(List<Lecture> lectures) {
        return lectureJpaRepository.saveAll(lectures);
    }
}
