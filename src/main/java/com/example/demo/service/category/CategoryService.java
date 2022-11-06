package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryListResponseDto;
import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.entity.category.Category;
import com.example.demo.exeption.category.CategoryDuplicateException;
import com.example.demo.exeption.category.CategoryListEmptyException;
import com.example.demo.exeption.category.CategoryNotFoundException;
import com.example.demo.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryListResponseDto> findAll() {

        List<Category> lst = categoryRepository.findAll();

        if(lst.isEmpty()) {
            throw new CategoryListEmptyException();
        }

        return lst.stream()
                .map(s-> new CategoryListResponseDto().toDto(s)).collect(Collectors.toList());
    }

    @Transactional
    public void addCategory(CategoryRequestDto requestDto) {
        if(categoryRepository.existsCategoryByCategoryName(requestDto.getCategory())) {
            throw new CategoryDuplicateException();
        }
        Category addItem = Category.builder().categoryName(requestDto.getCategory()).build();
        categoryRepository.save(addItem);
    }

    @Transactional
    public void deleteCategory(long id) {
        if(categoryRepository.existsCategoryById(id)) {
            throw new CategoryNotFoundException();
        }
        categoryRepository.deleteById(id);
    }
}
