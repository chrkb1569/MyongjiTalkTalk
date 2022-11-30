package com.example.demo.service.board;

import com.example.demo.dto.board.BoardEditRequestDto;
import com.example.demo.dto.board.BoardListResponseDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.dto.board.BoardWriteRequestDto;
import com.example.demo.dto.comment.CommentDto;
import com.example.demo.entity.board.Board;
import com.example.demo.entity.category.Category;
import com.example.demo.exeption.board.BoardNotFoundException;
import com.example.demo.exeption.board.ListEmptyException;
import com.example.demo.exeption.board.WriterNotMatchException;
import com.example.demo.exeption.category.CategoryNotFoundException;
import com.example.demo.repository.board.BoardRepository;
import com.example.demo.repository.category.CategoryRepository;
import com.example.demo.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<BoardListResponseDto> getBoards(long categoryId) {
        if(!categoryRepository.existsCategoryById(categoryId)) {
            throw new CategoryNotFoundException();
        }

        List<Board> lst = boardRepository.findAllByCategoryId(categoryId);

        if(lst.isEmpty()) {
            throw new ListEmptyException();
        }

        return lst.stream().map(s -> new BoardListResponseDto().toDto(s))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(long categoryId, long id) {
        if(!categoryRepository.existsCategoryById(categoryId)) {
            throw new CategoryNotFoundException();
        }

        Board findItem = boardRepository.findBoardByCategoryIdAndId(categoryId, id).orElseThrow(BoardNotFoundException::new);

        if(!commentRepository.existsCommentByBoardId(id)) {
            return new BoardResponseDto(findItem);
        }

        List<CommentDto> lst = commentRepository.findAllByBoardId(id)
                .stream().map(s -> new CommentDto().toDto(s)).collect(Collectors.toList());

        return new BoardResponseDto(findItem, lst);
    }

    @Transactional
    public void write(BoardWriteRequestDto requestDto, long categoryId) {

        Category findItem = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String writer = authentication.getName();

        Board board = Board.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .writer(writer)
                .category(findItem)
                .build();

        boardRepository.save(board);
    }

    @Transactional
    public void edit(long categoryId, long id, BoardEditRequestDto requestDto) {
        if(!categoryRepository.existsCategoryById(categoryId)) {
            throw new CategoryNotFoundException();
        }

        Board findItem = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        String writer = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!writer.equals(findItem.getWriter())) {
            throw new WriterNotMatchException();
        }

        findItem.setContent(requestDto.getContent());
    }

    @Transactional
    public void delete(long categoryId, long id) {
        if(!categoryRepository.existsCategoryById(categoryId)) {
            throw new CategoryNotFoundException();
        }

        Board findItem = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        String writer = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!writer.equals(findItem.getWriter())) {
            throw new WriterNotMatchException();
        }

        boardRepository.deleteById(id);
    }
}
