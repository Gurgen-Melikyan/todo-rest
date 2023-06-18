package com.example.todorest.service;

import com.example.todorest.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findUsers();
    Optional<User> findById(int id);
    User addUser(User user);
    void deleteById(int id);
    Optional<User> findByEmail(String email);

    boolean existsById(int id);
}
