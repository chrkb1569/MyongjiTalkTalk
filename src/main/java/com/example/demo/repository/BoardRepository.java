package com.example.demo.repository;

import com.example.demo.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    boolean existsBoardById(Long id);
}
