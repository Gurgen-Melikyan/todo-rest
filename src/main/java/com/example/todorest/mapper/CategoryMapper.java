package com.example.todorest.mapper;

import com.example.todorest.dto.CategoryDto;
import com.example.todorest.dto.CreateCategoryRequestDto;
import com.example.todorest.dto.CreateCategoryResponseDto;
import com.example.todorest.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category map(CreateCategoryRequestDto dto);

    CreateCategoryResponseDto map(Category category);
    CategoryDto mapToDto(Category entity);
}
