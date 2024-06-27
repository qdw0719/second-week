package com.hanghae.lecture.infrastructure.entity;

import com.hanghae.lecture.infrastructure.entity.dto.LectureDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name="HB_LECTURE")
public class Lecture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String lectureCode;
    private int lectureLimit;
    private String openDay;
    private String openTime;


    public LectureDto toDto() {
        return new LectureDto(id, title, lectureCode, lectureLimit, openDay, openTime);
    }

    public static Lecture fromDto(LectureDto dto) {
        return new Lecture(dto.getId(), dto.getTitle(), dto.getLectureCode(), dto.getLectureLimit(), dto.getOpenDay(), dto.getOpenTime());
    }
}
