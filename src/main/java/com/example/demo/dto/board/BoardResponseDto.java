package com.example.demo.dto.board;

import com.example.demo.dto.comment.CommentDto;
import com.example.demo.entity.board.Board;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardResponseDto {
    private String title;
    private String content;
    private String writer;
    private List<CommentDto> comment;

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
    }

    public BoardResponseDto(Board board, List<CommentDto> lst) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.comment = lst;
    }
}
