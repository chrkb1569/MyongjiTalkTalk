package com.example.demo.service.comment;

import com.example.demo.dto.comment.CommentCreateRequestDto;
import com.example.demo.entity.board.Board;
import com.example.demo.entity.comment.Comment;
import com.example.demo.exeption.board.BoardNotFoundException;
import com.example.demo.exeption.board.WriterNotMatchException;
import com.example.demo.exeption.comment.CommentNotFoundException;
import com.example.demo.repository.board.BoardRepository;
import com.example.demo.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void makeComment(long boardId, CommentCreateRequestDto requestDto) {
        Board findItem = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);

        String writer = SecurityContextHolder.getContext().getAuthentication().getName();

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .writer(writer)
                .board(findItem)
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(long commentId) {
        Comment findItem = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!findItem.getWriter().equals(authentication.getName())) {
            throw new WriterNotMatchException();
        }

        commentRepository.deleteById(commentId);
    }
}
