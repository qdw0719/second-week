package com.hanghae.lecture.presentation.controller;

//import com.hanghae.lecture.common.exception.DuplicateLectureRequestException;
//import com.hanghae.lecture.common.exception.LectureFullException;
//import com.hanghae.lecture.common.exception.LectureNotOpenException;
import com.hanghae.lecture.presentation.dto.LectureDto;
import com.hanghae.lecture.presentation.dto.LectureUserDto;
import com.hanghae.lecture.presentation.service.LectureService;
import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Slf4j
@RequiredArgsConstructor
@RestController @RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

//    @PostMapping("/apply")
//    public ResponseEntity<String> applyLecture(@RequestParam Long lectureId, @RequestParam Long userId) {
//        try {
//            lectureService.applyLecture(lectureId, userId);
//            return ResponseEntity.ok("Lecture application successful");
//        } catch (DuplicateLectureRequestException | LectureFullException | LectureNotOpenException e) {
//            log.error("Lecture application failed: lectureId={}, userId={}, error={}", lectureId, userId, e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        } catch (Exception e) {
//            log.error("Unexpected error during lecture application: lectureId={}, userId={}, error={}", lectureId, userId, e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
//        }
//    }

    @PostMapping("/apply")
    public ResponseEntity<LectureUserDto> applyLecture(@RequestParam Long lectureId, @RequestParam Long userId) {
        LectureUserDto result = lectureService.applyLecture(lectureId, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/application/{userId}")
    public ResponseEntity<Boolean> checkLectureStatus(@PathVariable("userId") Long userId, @RequestParam Long lectureId) {
        boolean status = lectureService.checkLectureStatus(lectureId, userId);
        return ResponseEntity.ok(status);
    }

    @GetMapping
    public ResponseEntity<List<LectureDto>> getAllLectures() {
        List<LectureDto> lectures = lectureService.getAllLectures();
        return ResponseEntity.ok(lectures);
    }
}
