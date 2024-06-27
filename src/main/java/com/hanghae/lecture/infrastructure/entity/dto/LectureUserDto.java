package com.hanghae.lecture.infrastructure.entity.dto;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class LectureUserDto {
    private Long id;
    private Long lectureId;
    private Long userId;
    private int successOrder;
}
