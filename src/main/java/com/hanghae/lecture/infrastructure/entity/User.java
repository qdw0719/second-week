package com.hanghae.lecture.infrastructure.entity;

import com.hanghae.lecture.infrastructure.entity.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Table(name="HB_USER")
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;

    public UserDto toDto() {
        return new UserDto(id, userName);
    }

    public static User fromDto(UserDto dto) {
        return new User(dto.getId(), dto.getUserName());
    }
}
