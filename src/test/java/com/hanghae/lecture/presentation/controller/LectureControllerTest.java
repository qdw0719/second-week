package com.hanghae.lecture.presentation.controller;

import com.hanghae.lecture.domain.model.Lecture;
import com.hanghae.lecture.domain.model.User;
import com.hanghae.lecture.domain.repository.LectureRepository;
import com.hanghae.lecture.domain.repository.LectureUserRepository;
import com.hanghae.lecture.domain.repository.UserRepository;
import com.hanghae.lecture.presentation.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ActiveProfiles("test")
@SpringBootTest @AutoConfigureMockMvc
@Transactional
class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureUserRepository lectureUserRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 데이터 초기화
        lectureUserRepository.deleteAll();
        lectureRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test @DisplayName("유효하지 않은 사용자로 강의 신청 시도")
    void applyLectureWithInvalidUserTest() throws Exception {
        User user = new User(null, "user1");
        userRepository.save(user);

        Lecture lecture = new Lecture(null, "Lecture01", "L_0001", LocalDateTime.now(), 30);
        lectureRepository.save(lecture);

        mockMvc.perform(post("/lectures/apply")
                        .param("lectureId", lecture.getId().toString())
                        .param("userId", "999"))  // 존재하지 않는 userId
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("강의 신청 테스트")
    void applyLectureTest() throws Exception {
        User user = new User(null, "user1");
        userRepository.save(user);
        Lecture lecture = new Lecture(null, "Lecture02", "L_0002", LocalDateTime.now(), 30);
        lectureRepository.save(lecture);

        mockMvc.perform(post("/lectures/apply")
                        .param("lectureId", lecture.getId().toString())
                        .param("userId", user.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectureId", is(lecture.getId().intValue())))
                .andExpect(jsonPath("$.userId", is(user.getId().intValue())));
    }

    @Test @DisplayName("강의 신청 여부 조회 테스트")
    void checkLectureStatusTest() throws Exception {
        User user = new User(null, "user1");
        userRepository.save(user);
        Lecture lecture = new Lecture(null, "Lecture03", "L_0003", LocalDateTime.now(), 30);
        lectureRepository.save(lecture);

        // 먼저 수강 신청
        mockMvc.perform(post("/lectures/apply")
                        .param("lectureId", lecture.getId().toString())
                        .param("userId", user.getId().toString()))
                .andExpect(status().isOk());

        // 신청 여부 확인
        mockMvc.perform(get("/lectures/application/{userId}", user.getId())
                        .param("lectureId", lecture.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    @Test @DisplayName("강의 신청 여부 조회 실패 테스트")
    void checkApplicationStatusFailTest() throws Exception {
        User user = new User(null, "user1");
        userRepository.save(user);
        Lecture lecture = new Lecture(null, "Lecture04", "L_0004", LocalDateTime.now(), 30);
        lectureRepository.save(lecture);

        // 수강 신청하지 않은 상태에서 신청 여부 확인
        mockMvc.perform(get("/lectures/application/{userId}", user.getId())
                        .param("lectureId", lecture.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test @DisplayName("모든 강의 조회 테스트")
    @Transactional
    void getAllLecturesTest() throws Exception {
        Lecture lecture1 = new Lecture(null, "Lecture05", "L_0005", LocalDateTime.now(), 30);
        Lecture lecture2 = new Lecture(null, "Lecture06", "L_0006", LocalDateTime.now(), 30);
        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);

        mockMvc.perform(get("/lectures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].lectureCode", is("L_0005")))
                .andExpect(jsonPath("$[1].lectureCode", is("L_0006")));
    }

    @Test @DisplayName("중복 신청 실패 테스트")
    void duplicateLectureTest() throws Exception {
        User user = new User(null, "user1");
        userRepository.save(user);
        Lecture lecture = new Lecture(null, "Lecture07", "L_0007", LocalDateTime.now(), 30);
        lectureRepository.save(lecture);

        // 첫 번째 신청 성공
        mockMvc.perform(post("/lectures/apply")
                        .param("lectureId", lecture.getId().toString())
                        .param("userId", user.getId().toString()))
                .andExpect(status().isOk());

        // 두 번째 신청 시도, 중복 신청 실패
        mockMvc.perform(post("/lectures/apply")
                        .param("lectureId", lecture.getId().toString())
                        .param("userId", user.getId().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("강의 오픈 전 신청 실패 테스트")
    void lectureNotOpenTest() throws Exception {
        User user = new User(null, "user1");
        userRepository.save(user);
        Lecture lecture = new Lecture(null, "Lecture08", "L_0008", LocalDateTime.of(2024, 12, 31, 23, 59), 30);
        lectureRepository.save(lecture);

        mockMvc.perform(post("/lectures/apply")
                        .param("lectureId", lecture.getId().toString())
                        .param("userId", user.getId().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("다수 유저의 강의 신청 테스트")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void concurrentApplyLectureTest() throws Exception {
        Lecture lecture = new Lecture(null, "Lecture09", "L_0009", LocalDateTime.now(), 30);
        lectureRepository.save(lecture);

        List<UserDto> users = new ArrayList<>();
        int userCount = 50;
        for (int i = 1; i <= userCount; i++) {
            User user = new User(null, "User" + i);
            User savedUser = userRepository.save(user);
            users.add(savedUser.toDto());
//            System.out.println("Saved User ID: " + savedUser.getId()); // 사용자 저장 로그 추가
        }

        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            int index = i;
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                try {
                    // error code return
                    return mockMvc.perform(post("/lectures/apply")
                                    .param("lectureId", lecture.getId().toString())
                                    .param("userId", users.get(index).getId().toString()))
                            // 30명만 ok 떨어질거고 20명은 400 Bad Request : 해당강의 인원 마감 에러 메세지 출력
                            // 그러므로 200 ok 체크할 수 없음.
                            // => CompletableFuture<Void> -> CompletableFuture<Integer> 수정하고 responseCode 출력하도록 변경
                            // 에러코드로 실패유무 판단,,
//                            .andExpect(status().isOk());
                            .andReturn().getResponse().getStatus();
                } catch (Exception e) {
                    e.printStackTrace();  // 예외 발생 시 stacktrace 출력
//                    System.err.println("Error applying lecture for user ID: " + users.get(index).getId());
                    return 500;
                }
            });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long successedApplyLecture = lectureUserRepository.countByLectureId(lecture.getId());

        // 30명까지만 성공함?
        assertEquals(30, successedApplyLecture);

        // 31번째부터는 실패함?
        long failedApplyLecture = futures.stream().filter(future -> {
                    try {
                        // 인원마감 400 error 발생 = 20개
                        return future.get() == 400;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();

        assertEquals(20, failedApplyLecture);
    }
}
