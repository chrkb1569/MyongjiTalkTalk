package com.example.demo.controller;

import com.example.demo.dto.security.TokenRequestDto;
import com.example.demo.dto.security.UserSignInRequestDto;
import com.example.demo.dto.security.UserSignUpRequestDto;
import com.example.demo.response.Response;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid UserSignUpRequestDto requestDto) {
        userService.singUp(requestDto);
    }

    @GetMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@RequestBody @Valid UserSignInRequestDto requestDto) {
        return Response.success(userService.signIn(requestDto));
    }

    @GetMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    public Response reIssue(@RequestBody @Valid TokenRequestDto requestDto) {
        return Response.success(userService.reIssue(requestDto));
    }
}
