package com.hanghae.lecture.presentation.service;

import com.hanghae.lecture.common.exception.*;
import com.hanghae.lecture.domain.model.*;
import com.hanghae.lecture.domain.repository.*;
import com.hanghae.lecture.presentation.dto.*;
import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Slf4j
@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureUserRepository lectureUserRepository;
    private final UserRepository userRepository;
    private final LectureHistoryRepository lectureHistoryRepository;

    private UserDto userValidation(long id) throws UserNotFoundException {
//        log.debug("Validating user with ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("[id : %d] 유저가 존재하지 않습니다.", id))
        );

        return user.toDto();
    }

    private LectureDto lectureValidation(long id) throws LectureNotFoundException {
        // 강의 정보를 가져올 때 PESSIMISTIC_WRITE 락을 사용하여 동시 접근을 방지
        Lecture lecture = lectureRepository.findByIdWithPessimisticLock(id).orElseThrow(() ->
                new LectureNotFoundException(String.format("[id: %d] 강의가 존재하지 않습니다.", id))
        );

        return lecture.toDto();
    }

    private String generateLectureCode() {
        long lectureCount = lectureRepository.count();
        return String.format("L_%04d", lectureCount + 1);
    }

    public LectureDto createLecture(String title, LocalDateTime openTime, int limit) {
        String lectureCode = generateLectureCode();
        Lecture lecture = new Lecture(null, title, lectureCode, openTime, limit);
        Lecture newLecture = lectureRepository.save(lecture);
        return newLecture.toDto();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public LectureUserDto applyLecture(Long lectureId, Long userId) throws DuplicateLectureRequestException, LectureFullException, LectureNotOpenException {
//        log.debug("Applying lecture: lectureId={}, userId={}", lectureId, userId);  // 사용자 ID와 강의 ID를 로그로 출력
        UserDto user = userValidation(userId);
        LectureDto lecture = lectureValidation(lectureId);

        // 사용자가 이미 해당 강의에 신청했는지 확인
        if (lectureUserRepository.findByLectureIdAndUserId(lectureId, userId).isPresent()) {
            throw new DuplicateLectureRequestException(String.format("[%s] 이미 해당 강의를 수강중입니다.", lecture.getTitle()));
        }

        long currentLectureUsersCount = lectureUserRepository.countByLectureId(lecture.getId());

        // 강의가 아직 열리지 않은 경우 예외 발생
        if (lecture.getLectureOpenTime().isAfter(LocalDateTime.now())) {
            throw new LectureNotOpenException(String.format("[%s] 해당 강의는 아직 오픈되지 않았습니다.", lecture.getTitle()));
        }
        // 강의 정원이 초과된 경우 예외 발생
        if (currentLectureUsersCount >= lecture.getLectureLimit()) {
            saveLectureHistory(lectureId, userId, LectureActionStatus.FAIL);
            throw new LectureFullException(String.format("[%s] 해당 강의는 인원이 가득 찼습니다.", lecture.getTitle()));
        }

        // 강의 신청 정보 저장
        LectureUser lectureUser = new LectureUser(null, lecture.getId(), user.getId());
        LectureUser savedLectureUser = lectureUserRepository.save(lectureUser);

        saveLectureHistory(lectureId, userId, LectureActionStatus.SUCCESS);

        return savedLectureUser.toDto();
    }

    @Transactional
    public void saveLectureHistory(Long lectureId, Long userId, LectureActionStatus status) {
        LectureHistory lectureHistory = new LectureHistory(null, lectureId, userId, status, LocalDateTime.now());
        lectureHistoryRepository.save(lectureHistory);
    }

    public boolean checkLectureStatus(Long lectureId, Long userId) {
        return lectureUserRepository.findByLectureIdAndUserId(lectureId, userId).isPresent();
    }

    public List<LectureDto> getAllLectures() {
        List<Lecture> lectures = lectureRepository.findAll();
        return lectures.stream().map(Lecture::toDto).collect(Collectors.toList());
    }
}
