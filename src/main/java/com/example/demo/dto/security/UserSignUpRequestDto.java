package com.example.demo.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSignUpRequestDto {

    @NotNull(message = "가입하기 위한 사용자 아이디를 입력해주세요!")
    private String username; // 사용자 아이디

    @NotNull(message = "가입하기 위한 사용자 비밀번호를 입력해주세요!")
    private String password; // 사용자 비밀번호

    @NotNull(message = "가입하기 위한 사용자의 이름을 입력해주세요!")
    @Size(min = 2, message = "사용자의 이름이 너무 짧습니다. 다시 한번 확인해주세요.")
    private String name; // 사용자 이름

    @NotNull(message = "가입하기 위한 사용자의 학과를 입력해주세요!")
    private String subject; // 사용자 학과

    @NotBlank(message = "가입하기 위한 사용자 학번을 입력해주세요!")
    private String studentId; // 사용자 학번
}
