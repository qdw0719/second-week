package com.hanghae.lecture.presentation.service;

import com.hanghae.lecture.Business.service.LectureService;
import com.hanghae.lecture.infrastructure.entity.dto.LectureDto;
import com.hanghae.lecture.infrastructure.entity.dto.LectureUserDto;
import com.hanghae.lecture.common.exception.*;
import com.hanghae.lecture.Business.repository.*;
import com.hanghae.lecture.infrastructure.entity.Lecture;
import com.hanghae.lecture.infrastructure.entity.LectureHistory;
import com.hanghae.lecture.infrastructure.entity.LectureUser;
import com.hanghae.lecture.infrastructure.entity.User;
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
        lecture = new Lecture(1L, "Lecture01", "L_0001",30, "THURSDAY", "21:00");

        when(userRepository.save(user)).thenReturn(user);
        when(lectureRepository.save(lecture)).thenReturn(lecture);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));
        when(lectureRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(lecture));
    }

    @Test @DisplayName("강의 Code 생성 테스트")
    void generateLectureCodeTest() {
        when(lectureRepository.count()).thenReturn(0L);

        lecture = new Lecture(1L, "Lecture01", "L_0001", 30, "THURSDAY", "21:00");
        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

        LectureDto generatedLecture = lectureService.generatedLecture("Lecture01",30, "THURSDAY", "21:00");

        assertEquals("L_0001", generatedLecture.getLectureCode());
    }

    @Test @DisplayName("모든 강의 조회 테스트")
    void getAllLecturesTest() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, "Lecture01", "L_0001",30, "THURSDAY", "21:00"),
                new Lecture(2L, "Lecture02", "L_0002", 30, "SUNDAY", "13:00"),
                new Lecture(3L, "Lecture03", "L_0003", 30, "SUNDAY", "18:00")
        );

        when(lectureRepository.findAll()).thenReturn(lectures);

        List<LectureDto> lectureDtoList = lectureService.getAllLectures();

        assertEquals(3, lectureDtoList.size());
        assertEquals("L_0001", lectureDtoList.get(0).getLectureCode());
        assertEquals("L_0002", lectureDtoList.get(1).getLectureCode());
    }

    @Test @Transactional @DisplayName("수강 신청 테스트")
    void applyLecture() throws LectureFullException, LectureNotOpenException, DuplicateLectureRequestException {
        long lectureId = 1L;
        long userId = 1L;

        when(lectureUserRepository.countByLectureId(lectureId)).thenReturn(0L);
        when(lectureUserRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(Optional.empty());

        LectureUser savedLectureUser = new LectureUser(1L, lectureId, userId, 1);
        when(lectureUserRepository.save(any(LectureUser.class))).thenReturn(savedLectureUser);

        LectureUserDto lectureUserDto = lectureService.applyLecture(lectureId, userId);

        assertNotNull(lectureUserDto);
        assertEquals(lectureId, lectureUserDto.getLectureId());
        assertEquals(userId, lectureUserDto.getUserId());
        assertEquals(1L, lectureUserDto.getId());
        assertEquals(1, lectureUserDto.getSuccessOrder());
    }

    @Test @DisplayName("수강 신청 상태 확인 테스트")
    void checkLectureStatusTest() {
        long lectureId = 1L;
        Long userId = 1L;

        when(lectureUserRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(Optional.of(new LectureUser()));

        boolean status = lectureService.checkLectureStatus(lectureId, userId);

        assertTrue(status);
    }

    @Test @DisplayName("수강 신청 가능 시간이 아닐 때 신청")
    void applyLectureNotOpenTest() {
        long lectureId = 1L;
        long userId = 1L;

        assertThrows(LectureNotOpenException.class, () -> lectureService.applyLecture(lectureId, userId));
    }

    @Test @DisplayName("중복 신청 실패 테스트")
    void duplicateApplicationTest() {
        long lectureId = 1L;
        long userId = 1L;

        when(lectureUserRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(Optional.of(new LectureUser(1L, lectureId, userId, 1)));

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
