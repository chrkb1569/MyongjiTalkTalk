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

    @GetMapping("/boards")
    @ResponseStatus(HttpStatus.OK)
    public Response getBoards() {
        return Response.success(boardService.getBoards());
    }

    @GetMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response getBoard(@PathVariable long id) {
        return Response.success(boardService.getBoard(id));
    }

    @PostMapping("/boards")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody BoardWriteRequestDto requestDto) {
        boardService.write(requestDto);
    }

    @PutMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void edit(@PathVariable long id, @RequestBody @Valid BoardEditRequestDto requestDto) {
        boardService.edit(id, requestDto);
    }

    @DeleteMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        boardService.delete(id);
    }
}
