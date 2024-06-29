package com.hanghae.lecture.domain.model;

import com.hanghae.lecture.presentation.dto.LectureDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Table(name="HB_LECTURE")
@Entity
public class Lecture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String lectureCode;
    private LocalDateTime lectureOpenTime;
    private int lectureLimit;

    public LectureDto toDto() {
        return new LectureDto(id, title, lectureCode, lectureOpenTime, lectureLimit);
    }

    public static Lecture fromDto(LectureDto dto) {
        return new Lecture(dto.getId(), dto.getTitle(), dto.getLectureCode(), dto.getLectureOpenTime(), dto.getLectureLimit());
    }
}
