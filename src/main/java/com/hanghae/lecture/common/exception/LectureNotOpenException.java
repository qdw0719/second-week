package com.hanghae.lecture.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @name LectureNotOpenException
 * @description 아직 오픈하지 않은 강의에 대해 수강신청 요청을 할 경우 발생하는 Exception
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LectureNotOpenException extends LectureException {
    public LectureNotOpenException(String message) {
        super(message);
    }
}
