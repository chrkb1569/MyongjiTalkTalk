package com.example.demo.advice;

import com.example.demo.exeption.board.BoardNotFoundException;
import com.example.demo.exeption.board.ListEmptyException;
import com.example.demo.exeption.board.WriterNotMatchException;
import com.example.demo.exeption.bus.EmptyBusStationException;
import com.example.demo.exeption.bus.StationNotFoundException;
import com.example.demo.exeption.bus.WrongBusInfoException;
import com.example.demo.exeption.category.CategoryDuplicateException;
import com.example.demo.exeption.category.CategoryListEmptyException;
import com.example.demo.exeption.category.CategoryNotFoundException;
import com.example.demo.exeption.comment.CommentNotFoundException;
import com.example.demo.exeption.jwt.WrongRefreshTokenException;
import com.example.demo.exeption.user.DuplicateStudentIdException;
import com.example.demo.exeption.user.DuplicateUsernameException;
import com.example.demo.exeption.user.LoginFailureException;
import com.example.demo.exeption.user.UserNotFoundException;
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
    @ExceptionHandler(DuplicateStudentIdException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response duplicateEmailException(DuplicateStudentIdException e) {
        return Response.failure(409, e.getMessage() + "는 이미 가입된 학번입니다. 다시 입력해주세요");
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

    @ExceptionHandler(ListEmptyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response listEmptyException() {
        return Response.failure(404,"게시글이 존재하지 않습니다.");
    }

    @ExceptionHandler(BoardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response boardNotFoundException() {
        return Response.failure(404, "해당 게시물이 존재하지 않습니다.");
    }

    @ExceptionHandler(WriterNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response writerNotMatchException() {
        return Response.failure(400, "현재 로그인한 사용자와 작성자의 정보가 일치하지 않습니다.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response userNotFoundException() {
        return Response.failure(404, "해당 정보를 가지고 있는 사용자를 찾지 못하였습니다.");
    }

    @ExceptionHandler(StationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response stationNotFoundException(StationNotFoundException e) {
        return Response.failure(404, e.getMessage() + "는 존재하지 않는 정류장입니다. 다시 한번 확인해주세요.");
    }

    @ExceptionHandler(WrongBusInfoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response wrongBusInfoException() {
        return Response.failure(400, "버스 정보를 다시 한번 확인해주세요.");
    }

    @ExceptionHandler(EmptyBusStationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response emptyBusStationException() {
        return Response.failure(404, "현재 도착 예정인 버스가 존재하지 않습니다.");
    }

    @ExceptionHandler(CategoryListEmptyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryListEmptyException() {
        return Response.failure(404, "카테고리가 존재하지 않습니다.");
    }

    @ExceptionHandler(CategoryDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response categoryDuplicateException() {
        return Response.failure(409, "이미 존재하는 카테고리입니다.");
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException() {
        return Response.failure(404, "해당 Id에 해당하는 카테고리를 찾지 못하였습니다.");
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response commentNotFoundException() {
        return Response.failure(404, "해당 Id에 해당하는 댓글을 찾지 못하였습니다.");
    }
}
