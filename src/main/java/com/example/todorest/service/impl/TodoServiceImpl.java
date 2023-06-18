package com.example.todorest.service.impl;

import com.example.todorest.entity.*;
import com.example.todorest.repository.TodoRepository;
import com.example.todorest.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Override
    public List<Todo> findTodos() {
        return todoRepository.findAll();
    }

    @Override
    public boolean existsById(int id) {
        return todoRepository.existsById(id);
    }

    @Override
    public Optional<Todo> findById(int id) {
        return todoRepository.findById(id);
    }

    @Override
    public List<Todo> findTodosByUser(User user) {
        List<Todo> todos;
        if (user.getType() == Type.ADMIN) {
            todos = todoRepository.findAll();
        } else {
            todos = todoRepository.findByUser_Id(user.getId());
        }
        return todos;
    }

    @Override
    public List<Todo> findTodosByCategory(Category category) {
        return todoRepository.findByCategory_Id(category.getId());
    }


    @Override
    public Optional<Todo> findByTitle(String title) {
        return todoRepository.findByTitle(title);
    }

    @Override
    public List<Todo> findByStatus(Status status) {
        return todoRepository.findByStatus(status);
    }

    @Override
    public Todo addTodo(Todo todo) {
       return todoRepository.save(todo);
    }


    @Override
    public void deleteById(int id) {
        todoRepository.deleteById(id);
    }
}
