package com.example.demo.repository.category;

import com.example.demo.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsCategoryByCategoryName(String categoryName);
    boolean existsCategoryById(Long id);
}
