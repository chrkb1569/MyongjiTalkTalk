package com.example.demo.service.board;

import com.example.demo.dto.board.BoardEditRequestDto;
import com.example.demo.dto.board.BoardListResponseDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.dto.board.BoardWriteRequestDto;
import com.example.demo.entity.board.Board;
import com.example.demo.exeption.board.BoardNotFoundException;
import com.example.demo.exeption.board.ListEmptyException;
import com.example.demo.exeption.board.WriterNotMatchException;
import com.example.demo.repository.BoardRepository;
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

    @Transactional(readOnly = true)
    public List<BoardListResponseDto> getBoards() {

        List<Board> lst = boardRepository.findAll();

        if(lst.isEmpty()) {
            throw new ListEmptyException();
        }

        return lst.stream().map(s -> new BoardListResponseDto().toDto(s))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(long id) {
        Board findItem = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        return new BoardResponseDto(findItem.getTitle(), findItem.getContent(), findItem.getWriter());
    }

    @Transactional
    public void write(BoardWriteRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String writer = authentication.getName();

        Board board = Board.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .writer(writer)
                .build();

        boardRepository.save(board);
    }

    @Transactional
    public void edit(long id, BoardEditRequestDto requestDto) {
        Board findItem = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        String writer = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!writer.equals(findItem.getWriter())) {
            throw new WriterNotMatchException();
        }

        findItem.setContent(requestDto.getContent());
    }

    @Transactional
    public void delete(long id) {
        Board findItem = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        String writer = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!writer.equals(findItem.getWriter())) {
            throw new WriterNotMatchException();
        }

        boardRepository.deleteById(id);
    }
}
