package com.example.demo.repository.comment;

import com.example.demo.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsCommentById(long id);
    boolean existsCommentByBoardId(long id);
    List<Comment> findAllByBoardId(long id);
}
