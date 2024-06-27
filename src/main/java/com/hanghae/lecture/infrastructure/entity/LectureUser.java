package com.hanghae.lecture.infrastructure.entity;

import com.hanghae.lecture.infrastructure.entity.dto.LectureUserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "HB_LECTURE_USER")
public class LectureUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lectureId;
    private Long userId;

    private int successOrder;

    public LectureUserDto toDto() {
        return new LectureUserDto(id, lectureId, userId, successOrder);
    }

    public static LectureUser fromDto(LectureUserDto dto) {
        return new LectureUser(dto.getId(), dto.getLectureId(), dto.getUserId(), dto.getSuccessOrder());
    }
}
