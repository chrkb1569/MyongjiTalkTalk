package com.example.demo.controller.user;

import com.example.demo.dto.security.TokenRequestDto;
import com.example.demo.dto.user.*;
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

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@RequestBody @Valid UserSignInRequestDto requestDto) {
        return Response.success(userService.signIn(requestDto));
    }

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    public Response reIssue(@RequestBody @Valid TokenRequestDto requestDto) {
        return Response.success(userService.reIssue(requestDto));
    }

    @GetMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    public Response searchId(@RequestBody @Valid UserIdRequestDto requestDto) {
        return Response.success(userService.searchId(requestDto));
    }

    @GetMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public Response reIssuePassword(@RequestBody @Valid UserPasswordReissueRequestDto requestDto) {
        return Response.success(userService.reIssuePassword(requestDto));
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody @Valid UserPasswordChangeRequestDto requestDto) {
        userService.changePassword(requestDto);
    }
}
