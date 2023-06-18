package com.example.todorest.endpoit;

import com.example.todorest.dto.CategoryDto;
import com.example.todorest.dto.CreateCategoryRequestDto;
import com.example.todorest.dto.CreateCategoryResponseDto;
import com.example.todorest.entity.Category;
import com.example.todorest.mapper.CategoryMapper;
import com.example.todorest.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryEndpoint {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    @PostMapping()
    public ResponseEntity<CreateCategoryResponseDto> create(@RequestBody CreateCategoryRequestDto requestDto) {
        Optional<Category> byName = categoryService.findByName(requestDto.getName());
        if (byName.isEmpty()) {
            Category category = categoryMapper.map(requestDto);
            categoryService.save(category);
            return ResponseEntity.ok(categoryMapper.map(category));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        List<Category> all = categoryService.findCategories();
        if (all.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : all) {
            categoryDtos.add(categoryMapper.mapToDto(category));
        }
        return ResponseEntity.ok(categoryDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id) {
        if (categoryService.existsById(id)) {
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
