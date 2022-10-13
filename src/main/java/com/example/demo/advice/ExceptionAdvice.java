package com.example.demo.advice;

import com.example.demo.exeption.jwt.WrongRefreshTokenException;
import com.example.demo.exeption.user.DuplicateEmailException;
import com.example.demo.exeption.user.DuplicateUsernameException;
import com.example.demo.exeption.user.LoginFailureException;
import com.example.demo.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    // 회원 가입시, 이미 가입되어 있는 사용자 아이디를 입력하였을 경우
    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response duplicateUserInfoException(DuplicateUsernameException e) {
        return Response.failure(409, e.getMessage() + "는 이미 가입된 아이디입니다. 다시 입력해주세요");
    }

    // 회원 가입시, 이미 가입되어 있는 이메일을 입력하는 경우
    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response duplicateEmailException(DuplicateEmailException e) {
        return Response.failure(409, e.getMessage() + "는 이미 가입된 이메일입니다. 다시 입력해주세요");
    }

    // 가입되어 있지 않은 아이디를 입력하거나, 비밀번호 오류가 발생하였을 경우
    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response loginFailureException() {
        return Response.failure(404, "로그인에 실패하였습니다. 아이디와 비밀번호를 확인해주세요.");
    }

    @ExceptionHandler(WrongRefreshTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response wrongRefreshTokenException() {
        return Response.failure(400, "유효하지 않은 재발급 토큰입니다.");
    }
}
