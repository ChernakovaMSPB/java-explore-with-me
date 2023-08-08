package ru.practicum.main.mappers;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.model.Category;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDto(Page<Category> categories);

    Category toCategory(CategoryDto categoryDto);

    void update(CategoryDto categoryDto, @MappingTarget Category category);
}
