package com.example.demo.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSignInRequestDto {

    @NotNull(message = "로그인하기 위한 아이디를 입력해주세요")
    private String username;

    @NotNull(message = "로그인하기 위한 비밀번호를 입력해주세요")
    private String password;

    public UsernamePasswordAuthenticationToken getAuthentication() {
        return new UsernamePasswordAuthenticationToken(this.username, this.password);
    }
}
