package com.example.todorest.service;

import com.example.todorest.entity.Category;
import com.example.todorest.entity.Status;
import com.example.todorest.entity.Todo;
import com.example.todorest.entity.User;

import java.util.List;
import java.util.Optional;

public interface TodoService {

    List<Todo> findTodos();

    boolean existsById(int id);

    Optional<Todo> findById(int id);

    List<Todo> findTodosByUser(User user);

    List<Todo> findTodosByCategory(Category category);

    Todo addTodo(Todo todo);
    Optional<Todo> findByTitle(String title);
    List<Todo> findByStatus(Status status);
    void deleteById(int id);
}
