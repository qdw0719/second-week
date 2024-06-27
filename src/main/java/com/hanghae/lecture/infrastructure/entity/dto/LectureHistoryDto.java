package com.hanghae.lecture.infrastructure.entity.dto;

import com.hanghae.lecture.infrastructure.entity.LectureActionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class LectureHistoryDto {
    private Long id;
    private Long lectureId;
    private Long userId;
    private LectureActionStatus status;
    private LocalDateTime regDate;
}
