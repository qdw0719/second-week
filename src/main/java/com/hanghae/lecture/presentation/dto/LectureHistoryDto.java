package com.hanghae.lecture.presentation.dto;

import com.hanghae.lecture.domain.model.LectureActionStatus;
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
