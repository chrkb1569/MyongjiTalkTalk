package com.example.demo.dto.comment;

import com.example.demo.entity.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {
    private String writer;
    private String content;

    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getWriter(), comment.getContent());
    }
}
