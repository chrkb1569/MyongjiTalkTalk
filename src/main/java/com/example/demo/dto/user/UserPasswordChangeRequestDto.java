package com.example.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordChangeRequestDto {

    @NotNull(message = "변경할 비밀 번호를 입력해주세요")
    private String password;

}
