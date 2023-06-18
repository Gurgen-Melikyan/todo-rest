package com.example.todorest.repository;

import com.example.todorest.entity.Status;
import com.example.todorest.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo,Integer> {

    List<Todo> findByUser_Id(int id);
    List<Todo> findByCategory_Id(int id);

    Optional<Todo> findByTitle(String title);
    List<Todo> findByStatus(Status status);
}
