package com.hanghae.lecture.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @name UserNotFoundException
 * @description 유저 id 정보가 존재하지 않을 때 발생하는 Exception
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends LectureException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
