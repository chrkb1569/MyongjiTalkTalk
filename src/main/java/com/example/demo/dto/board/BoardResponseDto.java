package com.example.demo.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardResponseDto {
    private String title;
    private String content;
    private String writer;
}
