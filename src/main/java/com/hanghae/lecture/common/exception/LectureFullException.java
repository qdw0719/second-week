package com.hanghae.lecture.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @name LectureFullException
 * @description 강의 수강인원이 다 찼을 때부터 발생하는 Exception
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LectureFullException extends LectureException {
    public LectureFullException(String message) {
        super(message);
    }
}
