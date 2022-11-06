package com.example.demo.repository.board;

import com.example.demo.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findBoardByCategoryIdAndId(long categoryId, long Id);
    List<Board> findAllByCategoryId(long id);
}
