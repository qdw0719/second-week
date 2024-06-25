package com.hanghae.lecture.infrastructure.repository;

import com.hanghae.lecture.domain.model.LectureUser;
import com.hanghae.lecture.domain.repository.LectureUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class LectureUserRepositoryImpl implements LectureUserRepository {

    private final LectureUserJpaRepository lectureUserJpaRepository;

    @Override
    public List<LectureUser> findAll() {
        return lectureUserJpaRepository.findAll();
    }

    @Override
    public Optional<LectureUser> findByLectureIdAndUserId(Long lectureId, Long userId) {
        return lectureUserJpaRepository.findByLectureIdAndUserId(lectureId, userId);
    }

    @Override
    public LectureUser save(LectureUser lectureUser) {
        return lectureUserJpaRepository.save(lectureUser);
    }

    @Override
    public long countByLectureId(Long lectureId) {
        return lectureUserJpaRepository.countByLectureId(lectureId);
    }

    @Override
    public void deleteAll() {
        lectureUserJpaRepository.deleteAll();
    }
}
