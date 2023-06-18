package com.example.todorest.endpoit;

import com.example.todorest.dto.CreateTodoRequestDto;
import com.example.todorest.dto.TodoDto;
import com.example.todorest.dto.UpdateTodoRequestDto;
import com.example.todorest.entity.Category;
import com.example.todorest.entity.Status;
import com.example.todorest.entity.Todo;
import com.example.todorest.mapper.TodoMapper;
import com.example.todorest.security.CurrentUser;
import com.example.todorest.service.CategoryService;
import com.example.todorest.service.TodoService;
import com.example.todorest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoEndpoint {
    private final TodoService todoService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final TodoMapper todoMapper;


    @PostMapping
    public ResponseEntity<TodoDto> create(@RequestBody CreateTodoRequestDto createTodoRequestDto,
                                          @AuthenticationPrincipal CurrentUser currentUser) {
        Optional<Category> byId = categoryService.findById(createTodoRequestDto.getCategoryId());
        if (byId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Todo todo = todoMapper.map(createTodoRequestDto);
        todo.setStatus(Status.NOT_STARTED);
        todo.setUser(currentUser.getUser());
        todo.setCategory(byId.get());
        todoService.addTodo(todo);

        return ResponseEntity.ok(todoMapper.mapToDto(todo));

    }

    @GetMapping
    public ResponseEntity<List<TodoDto>> getTodoByUser(@AuthenticationPrincipal CurrentUser currentUser) {
        List<Todo> todosByUser = todoService.findTodosByUser(currentUser.getUser());
        if (todosByUser.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        List<TodoDto> todoDtos = new ArrayList<>();
        for (Todo todo : todosByUser) {
            todoDtos.add(todoMapper.mapToDto(todo));
        }
        return ResponseEntity.ok(todoDtos);
    }

    @GetMapping("/byStatus/{status}")
    public ResponseEntity<List<TodoDto>> getTodoByUser(@PathVariable("status") Status status,
                                                       @AuthenticationPrincipal CurrentUser currentUser) {

        List<Todo> todosByUser = todoService.findTodosByUser(currentUser.getUser());
        if (todosByUser.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        List<TodoDto> todoDtos = new ArrayList<>();
        for (Todo todo : todosByUser) {
            if (todo.getStatus() == status) {
                todoDtos.add(todoMapper.mapToDto(todo));
            }
        }

        return ResponseEntity.ok(todoDtos);
    }

    @GetMapping("byCategory/{id}")
    public ResponseEntity<List<TodoDto>> getTodoByCategory(@PathVariable("id") int id,
                                                           @AuthenticationPrincipal CurrentUser currentUser) {

        List<Todo> todosByUser = todoService.findTodosByUser(currentUser.getUser());
        if (todosByUser.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        List<TodoDto> todoDtos = new ArrayList<>();
        for (Todo todo : todosByUser) {
            if (todo.getCategory().getId() == id) {
                todoDtos.add(todoMapper.mapToDto(todo));
            }
        }

        return ResponseEntity.ok(todoDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable("id") int id, @RequestBody UpdateTodoRequestDto updateTodoRequestDto,
                                       @AuthenticationPrincipal CurrentUser currentUser) {
        Optional<Todo> byId = todoService.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (byId.get().getUser().getId() != currentUser.getUser().getId()) {
            return ResponseEntity.notFound().build();
        }
        Todo todoFromDb = byId.get();
        Todo todo = todoMapper.map(updateTodoRequestDto);
        if (todo.getStatus() != null) {
            todoFromDb.setStatus(todo.getStatus());
        }

        return ResponseEntity.ok(todoService.addTodo(todoFromDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id, @AuthenticationPrincipal CurrentUser currentUser) {
        Optional<Todo> byId = todoService.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (byId.get().getUser().getId() != currentUser.getUser().getId()) {
            return ResponseEntity.notFound().build();
        }
        if (todoService.existsById(id)) {
            todoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}