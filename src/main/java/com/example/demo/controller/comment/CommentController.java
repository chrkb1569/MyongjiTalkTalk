package com.example.demo.controller.comment;

import com.example.demo.dto.comment.CommentCreateRequestDto;
import com.example.demo.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{boardId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void makeComment(@PathVariable long boardId, @RequestBody @Valid CommentCreateRequestDto requestDto) {
        commentService.makeComment(boardId, requestDto);
    }

    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable long commentId) {
        commentService.deleteComment(commentId);
    }
}
