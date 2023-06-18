package com.example.todorest.mapper;

import com.example.todorest.dto.CreateTodoRequestDto;

import com.example.todorest.dto.TodoDto;
import com.example.todorest.dto.UpdateTodoRequestDto;
import com.example.todorest.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface TodoMapper {
    @Mapping(target = "category.id", source = "categoryId")
    Todo map(CreateTodoRequestDto dto);

    Todo map(UpdateTodoRequestDto dto);


    @Mapping(target = "categoryDto", source = "category")
    TodoDto mapToDto(Todo entity);
}
