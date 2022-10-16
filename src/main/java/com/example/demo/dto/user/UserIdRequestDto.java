package com.example.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdRequestDto {

    @NotNull(message = "아이디를 찾기 위한 학번을 입력해주세요")
    private String studentId;

    @NotNull(message = "아이디를 찾기 위한 사용자 이름을 입력해주세요")
    private String name;

    @NotNull(message = "아이디를 찾기 위한 사용자 학과를 입력해주세요")
    private String subject;
}
