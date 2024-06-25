package com.hanghae.lecture.presentation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class LectureDto {
    private Long id;
    private String title;
    private String lectureCode;
    private LocalDateTime lectureOpenTime;
    private int lectureLimit;
}
