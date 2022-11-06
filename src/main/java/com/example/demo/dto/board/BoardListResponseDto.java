package com.example.demo.dto.board;

import com.example.demo.dto.comment.CommentDto;
import com.example.demo.entity.board.Board;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardListResponseDto {

    private String title;
    private String content;
    private String writer;

    public BoardListResponseDto toDto(Board board) {
        return new BoardListResponseDto(board.getTitle(), board.getContent(), board.getWriter());
    }
}
