package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.ValidationException;
import ru.practicum.main.mappers.CategoryMapper;
import ru.practicum.main.model.Category;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.service.CategoryService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto addNewCategory(CategoryDto categoryDto) {
        boolean isExists = checkCategoryName(categoryDto);
        if (isExists) {
            throw new ValidationException("");
        }
        Category category = categoryMapper.toCategory(categoryDto);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ValidationException("The category is not empty.");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category categoryToUpdate = findCategoryById(catId);
        Category categoryName = categoryRepository.findCategoryByName(categoryDto.getName());
        if (categoryName != null && !Objects.equals(categoryName.getId(), catId)) {
            throw new ValidationException("The category is not empty");
        }

        if (categoryName != null && Objects.equals(categoryName.getId(), catId)) {
            categoryDto.setId(catId);
            return categoryDto;
        }
        if (categoryToUpdate.getId().equals(catId)) {
            categoryDto.setId(catId);
            return categoryDto;
        }
        categoryToUpdate = categoryRepository.save(categoryToUpdate);
        categoryMapper.update(categoryDto, categoryToUpdate);

        return categoryMapper.toCategoryDto(categoryToUpdate);
    }

    @Transactional(readOnly = true)
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category " + id + " is not found"));
    }

    public boolean checkCategoryName(CategoryDto categoryDto) {
        Category category = categoryRepository.findCategoryByName(categoryDto.getName());
        if (category == null) {
            return false;
        }
        if (categoryDto.getId() != null) {
            throw new ValidationException("Category name already exists.");
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(Pageable pageable) {
        return categoryMapper.toCategoryDto(categoryRepository.findAll(pageable));

    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long catId) {
        return categoryMapper.toCategoryDto(findCategoryById(catId));
    }
}