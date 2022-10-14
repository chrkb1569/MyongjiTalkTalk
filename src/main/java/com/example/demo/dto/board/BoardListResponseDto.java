package com.example.demo.dto.board;

import com.example.demo.entity.board.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
