package com.hanghae.lecture.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @name LectureException
 * @description 수강신청 프로그램에서 사용할 Custom Exception
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LectureException extends RuntimeException {
    public LectureException(String message) {
        super(message);
    }
}
