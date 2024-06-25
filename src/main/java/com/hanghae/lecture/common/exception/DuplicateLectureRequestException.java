package com.hanghae.lecture.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @name DuplicateLectureRequestException
 * @description 이미 수강신청을 한 과목일 때 발생하는 Exception
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateLectureRequestException extends LectureException {
    public DuplicateLectureRequestException(String message) {
        super(message);
    }
}
