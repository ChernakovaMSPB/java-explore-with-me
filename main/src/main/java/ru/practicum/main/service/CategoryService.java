package ru.practicum.main.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addNewCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategory(Long catId);
}
