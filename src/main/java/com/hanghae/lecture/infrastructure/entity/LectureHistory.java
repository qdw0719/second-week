package com.hanghae.lecture.infrastructure.entity;

import com.hanghae.lecture.infrastructure.entity.dto.LectureHistoryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Table(name="HB_LECTURE_HISTORY")
@Entity
public class LectureHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long lectureId;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private LectureActionStatus status;
    private LocalDateTime regDate;

    public LectureHistoryDto toDto() {
        return new LectureHistoryDto(id, lectureId, userId, status, regDate);
    }

    public static LectureHistory fromDto(LectureHistoryDto dto) {
        return new LectureHistory(dto.getId(), dto.getLectureId(), dto.getUserId(), dto.getStatus(), dto.getRegDate());
    }
}
