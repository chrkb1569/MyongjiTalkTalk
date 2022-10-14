package com.example.demo.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class BoardEditRequestDto {

    @NotNull(message = "수정할 게시글 내용을 입력해주세요!")
    private String content;

}
