package com.example.demo.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {
    @NotNull(message = "댓글에 입력하실 내용을 입력해주세요!")
    private String content;
}
