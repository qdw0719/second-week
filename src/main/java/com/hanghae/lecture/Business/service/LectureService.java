package com.hanghae.lecture.Business.service;

import com.hanghae.lecture.infrastructure.entity.dto.LectureDto;
import com.hanghae.lecture.infrastructure.entity.dto.LectureUserDto;
import com.hanghae.lecture.infrastructure.entity.dto.UserDto;
import com.hanghae.lecture.common.exception.*;
import com.hanghae.lecture.Business.repository.*;
import com.hanghae.lecture.infrastructure.entity.Lecture;
import com.hanghae.lecture.infrastructure.entity.LectureActionStatus;
import com.hanghae.lecture.infrastructure.entity.LectureHistory;
import com.hanghae.lecture.infrastructure.entity.LectureUser;
import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.exception.LockAcquisitionException;
//import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Slf4j
@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureUserRepository lectureUserRepository;
    private final UserRepository userRepository;
    private final LectureHistoryRepository lectureHistoryRepository;

    // 유저 검증
    private UserDto userValidation(long id) throws UserNotFoundException {
//        log.debug("Validating user with ID: {}", id);
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("[id : %d] 유저가 존재하지 않습니다.", id))
        ).toDto();
    }

    // 강의 검증
    private LectureDto lectureValidation(long id) throws LectureNotFoundException {
        // 강의 정보를 가져올 때 PESSIMISTIC_WRITE 락을 사용하여 동시 접근을 방지
        return lectureRepository.findByIdWithPessimisticLock(id).orElseThrow(() ->
                new LectureNotFoundException(String.format("[id: %d] 강의가 존재하지 않습니다.", id))
        ).toDto();
    }

    // 강의 코드 생성
    private String generateLectureCode(String title) {
        Optional<Lecture> existingLecture = lectureRepository.findFirstByTitle(title);
        if (existingLecture.isPresent()) {
            return existingLecture.get().getLectureCode();
        } else {
            long lectureCount = lectureRepository.count();
            return String.format("L_%04d", lectureCount + 1);
        }
    }

    public LectureDto generatedLecture(String title, int limit, String openDay, String openTime) {
        String lectureCode = generateLectureCode(title);
        Lecture lecture = new Lecture(null, title, lectureCode, limit, openDay, openTime);
        Lecture newLecture = lectureRepository.save(lecture);
        return newLecture.toDto();
    }

    // 수강신청
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public LectureUserDto applyLecture(Long lectureId, Long userId) throws DuplicateLectureRequestException, LectureFullException, LectureNotOpenException {
//            log.debug("Applying lecture: lectureId={}, userId={}", lectureId, userId);  // 사용자 ID와 강의 ID를 로그로 출력
        UserDto user = userValidation(userId);
        LectureDto lecture = lectureValidation(lectureId);

        LocalDateTime now = LocalDateTime.now();
        LocalDate nowDate = now.toLocalDate();
        LocalTime openTime = LocalTime.parse(lecture.getOpenTime() + ":00");
        LocalDateTime openDateTime = LocalDateTime.of(nowDate, openTime);

        boolean isOpen = lecture.getOpenDay().equals(now.getDayOfWeek().name())
                && !now.isBefore(openDateTime)
                && now.isBefore(openDateTime.plusHours(1));

        // 강의가 아직 열리지 않은 경우, 강의 신청시간이 지난 경우 예외 발생
        if (!isOpen) {
            throw new LectureNotOpenException(String.format("[%s] 수강신청 가능한 시간이 아닙니다", lecture.getTitle()));
        }

        // 사용자가 이미 해당 강의에 신청했는지 확인
        if (lectureUserRepository.findByLectureIdAndUserId(lectureId, userId).isPresent()) {
            throw new DuplicateLectureRequestException(String.format("[%s] 이미 해당 강의를 수강중입니다.", lecture.getTitle()));
        }

        long currentLectureUsersCount = lectureUserRepository.countByLectureId(lecture.getId());

        // 강의 정원이 초과된 경우 예외 발생
        if (currentLectureUsersCount >= lecture.getLectureLimit()) {
            saveLectureHistory(lectureId, userId, LectureActionStatus.FAIL);
            throw new LectureFullException(String.format("[%s] 해당 강의는 인원이 가득 찼습니다.", lecture.getTitle()));
        }

        // 강의 신청 정보 저장
        int successOrder = (int) currentLectureUsersCount + 1; // 성공순번 ex. 1등, 2등, 3등 ...
        LectureUser lectureUser = new LectureUser(null, lecture.getId(), user.getId(), successOrder);
        LectureUser savedLectureUser = lectureUserRepository.save(lectureUser);

        saveLectureHistory(lectureId, userId, LectureActionStatus.SUCCESS);

        return savedLectureUser.toDto();
    }

    // history저장
    @Transactional
    public void saveLectureHistory(Long lectureId, Long userId, LectureActionStatus status) {
        LectureHistory lectureHistory = new LectureHistory(null, lectureId, userId, status, LocalDateTime.now());
        lectureHistoryRepository.save(lectureHistory);
    }

    // 강의 신청 여부 확인
    public boolean checkLectureStatus(Long lectureId, Long userId) {
        return lectureUserRepository.findByLectureIdAndUserId(lectureId, userId).isPresent();
    }

    // 특정 사용자의 신청 완료 여부 확인
    public boolean checkLectureUserStatus(Long userId) {
        return lectureUserRepository.findByUserId(userId).isPresent();
    }

    // 강의 목록 가져오기
    public List<LectureDto> getAllLectures() {
        List<Lecture> lectures = lectureRepository.findAll();
        return lectures.stream().map(Lecture::toDto).collect(Collectors.toList());
    }
}
