package com.example.demo.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardWriteRequestDto {

    @NotNull(message = "게시글의 제목을 입력해주세요!")
    private String title;

    @NotNull(message = "게시글의 내용을 입력해주세요!")
    private String content;
}
