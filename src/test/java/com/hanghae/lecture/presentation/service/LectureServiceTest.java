package com.hanghae.lecture.presentation.service;

import com.hanghae.lecture.common.exception.*;
import com.hanghae.lecture.domain.model.*;
import com.hanghae.lecture.domain.repository.*;
import com.hanghae.lecture.presentation.dto.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(LectureService.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest @Transactional
class LectureServiceTest {

    @MockBean
    private LectureRepository lectureRepository;

    @MockBean
    private LectureUserRepository lectureUserRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LectureHistoryRepository lectureHistoryRepository;

    @Autowired
    private LectureService lectureService;

    private Lecture lecture;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User(1L, "user1");
        lecture = new Lecture(1L, "Lecture01", "L_0001", LocalDateTime.now().minusDays(1), 30);

        when(userRepository.save(user)).thenReturn(user);
        when(lectureRepository.save(lecture)).thenReturn(lecture);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));
        when(lectureRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(lecture));
    }

    @Test @DisplayName("강의 Code 생성 테스트")
    void generateLectureCodeTest() {
        when(lectureRepository.count()).thenReturn(0L);

        Lecture lecture = new Lecture(null, "Lecture01", "L_0001", LocalDateTime.now(), 30);
        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

        LectureDto generatedLecture = lectureService.createLecture("Lecture01", LocalDateTime.now(), 30);

        assertEquals("L_0001", generatedLecture.getLectureCode());
    }

    @Test @DisplayName("모든 강의 조회 테스트")
    void getAllLecturesTest() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, "Lecture01", "L_0001", LocalDateTime.now(), 30),
                new Lecture(2L, "Lecture02", "L_0002", LocalDateTime.now(), 30)
        );

        when(lectureRepository.findAll()).thenReturn(lectures);

        List<LectureDto> lectureDtoList = lectureService.getAllLectures();

        assertEquals(2, lectureDtoList.size());
        assertEquals("L_0001", lectureDtoList.get(0).getLectureCode());
        assertEquals("L_0002", lectureDtoList.get(1).getLectureCode());
    }

    @Test @Transactional @DisplayName("수강 신청 테스트")
    void applyLecture() throws LectureFullException, LectureNotOpenException, DuplicateLectureRequestException {
        long lectureId = 1L;
        long userId = 1L;

        when(lectureUserRepository.countByLectureId(lectureId)).thenReturn(0L);
        when(lectureUserRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(Optional.empty());

        LectureUser savedLectureUser = new LectureUser(1L, lectureId, userId);
        when(lectureUserRepository.save(any(LectureUser.class))).thenReturn(savedLectureUser);

        LectureUserDto lectureUserDto = lectureService.applyLecture(lectureId, userId);

        assertNotNull(lectureUserDto);
        assertEquals(lectureId, lectureUserDto.getLectureId());
        assertEquals(userId, lectureUserDto.getUserId());
        assertEquals(1L, lectureUserDto.getId());
    }

    @Test @DisplayName("수강 신청 상태 확인 테스트")
    void checkLectureStatusTest() {
        long lectureId = 1L;
        Long userId = 1L;

        when(lectureUserRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(Optional.of(new LectureUser()));

        boolean status = lectureService.checkLectureStatus(lectureId, userId);

        assertTrue(status);
    }

    // ------------------------------------------------
    // 수강신청 실패 테스트
    // ------------------------------------------------

    @Test @DisplayName("강의 오픈 전 신청 실패 테스트")
    void lectureNotOpenApplicationTest() {
        long lectureId = 1L;
        long userId = 1L;

        lecture.setLectureOpenTime(LocalDateTime.now().plusDays(1));

        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

        assertThrows(LectureNotOpenException.class, () -> lectureService.applyLecture(lectureId, userId));
    }

    @Test @DisplayName("중복 신청 실패 테스트")
    void duplicateApplicationTest() {
        long lectureId = 1L;
        long userId = 1L;

        when(lectureUserRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(Optional.of(new LectureUser(1L, lectureId, userId)));

        assertThrows(DuplicateLectureRequestException.class, () -> lectureService.applyLecture(lectureId, userId));
    }

    @Test @DisplayName("강의 정원 초과 실패 테스트")
    void lectureFullApplicationTest() {
        long lectureId = 1L;
        long userId = 1L;

        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
        when(lectureUserRepository.countByLectureId(lectureId)).thenReturn(30L);

        assertThrows(LectureFullException.class, () -> lectureService.applyLecture(lectureId, userId));
        verify(lectureHistoryRepository, times(1)).save(any(LectureHistory.class));
    }
}
