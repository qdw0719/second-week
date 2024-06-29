package com.hanghae.lecture.infrastructure.entity.dto;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class LectureDto {
    private Long id;
    private String title;
    private String lectureCode;
    private int lectureLimit;
    private String openDay;
    private String openTime;
}
