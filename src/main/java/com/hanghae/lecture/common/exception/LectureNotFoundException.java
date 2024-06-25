package com.hanghae.lecture.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @name LectureNotFoundException
 * @description 강의 id 정보가 존재하지 않을 때 발생하는 Exception
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LectureNotFoundException extends LectureException {
    public LectureNotFoundException(String message) {
        super(message);
    }
}
