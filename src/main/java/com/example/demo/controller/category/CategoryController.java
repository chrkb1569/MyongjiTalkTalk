package com.example.demo.controller.category;

import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.response.Response;
import com.example.demo.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category")
    @ResponseStatus(HttpStatus.OK)
    public Response findAll() {
        return Response.success(categoryService.findAll());
    }

    @PostMapping("/category")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCategory(@RequestBody @Valid CategoryRequestDto requestDto) {
        categoryService.addCategory(requestDto);
    }

    @DeleteMapping("/category/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
