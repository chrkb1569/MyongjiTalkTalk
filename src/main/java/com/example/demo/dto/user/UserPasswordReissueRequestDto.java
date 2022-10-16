package com.example.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordReissueRequestDto {

    @NotNull(message = "비밀번호를 찾기 위한 사용자 아이디를 입력해주세요")
    private String username;

    @NotNull(message = "비밀번호를 찾기 위한 사용자 학번을 입력해주세요")
    private String studentId;
}
