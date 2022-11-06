package com.example.demo.controller.board;

import com.example.demo.dto.board.BoardEditRequestDto;
import com.example.demo.dto.board.BoardWriteRequestDto;
import com.example.demo.response.Response;
import com.example.demo.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Response getBoards(@PathVariable Long categoryId) {
        return Response.success(boardService.getBoards(categoryId));
    }

    @GetMapping("/boards/{categoryId}/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response getBoard(@PathVariable long categoryId, @PathVariable long id) {
        return Response.success(boardService.getBoard(categoryId, id));
    }

    @PostMapping("/boards/{categoryId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody BoardWriteRequestDto requestDto, @PathVariable long categoryId) {
        boardService.write(requestDto, categoryId);
    }

    @PutMapping("/boards/{categoryId}/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void edit(@PathVariable long categoryId, @PathVariable long id, @RequestBody @Valid BoardEditRequestDto requestDto) {
        boardService.edit(categoryId, id, requestDto);
    }

    @DeleteMapping("/boards/{categoryId}/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long categoryId, @PathVariable long id) {
        boardService.delete(categoryId, id);
    }
}
