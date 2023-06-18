package com.example.todorest.service;

import com.example.todorest.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findCategories();
    Optional<Category> findById(int id);
    void save(Category category);
    Optional<Category> findByName(String name);
    void deleteById(int id);
    boolean existsById(int id);
}
