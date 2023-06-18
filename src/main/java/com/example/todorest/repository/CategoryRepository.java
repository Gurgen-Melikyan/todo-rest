package com.example.todorest.repository;

import com.example.todorest.entity.Category;
import com.example.todorest.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Optional<Category> findByName(String name);
}
