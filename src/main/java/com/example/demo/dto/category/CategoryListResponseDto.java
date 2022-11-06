package com.example.demo.dto.category;

import com.example.demo.entity.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListResponseDto {
    private Long id;
    private String category;

    public CategoryListResponseDto toDto(Category category) {
        return new CategoryListResponseDto(category.getId(), category.getCategoryName());
    }
}
