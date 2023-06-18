package com.example.todorest.endpoit;

import com.example.todorest.dto.*;
import com.example.todorest.entity.Category;
import com.example.todorest.entity.Type;
import com.example.todorest.entity.User;
import com.example.todorest.mapper.UserMapper;
import com.example.todorest.repository.UserRepository;
import com.example.todorest.security.CurrentUser;
import com.example.todorest.service.UserService;
import com.example.todorest.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserEndpoint {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;

    @PostMapping("/auth")
    public ResponseEntity<UserAuthResponseDto> auth(@RequestBody UserAuthRequestDto userAuthRequestDto) {
        Optional<User> byEmail = userService.findByEmail(userAuthRequestDto.getEmail());
        if (byEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = byEmail.get();
        if (!passwordEncoder.matches(userAuthRequestDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = jwtTokenUtil.generateToken(userAuthRequestDto.getEmail());
        return ResponseEntity.ok(new UserAuthResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody CreateUserRequestDto createUserRequestDto) {
        Optional<User> byEmail = userService.findByEmail(createUserRequestDto.getEmail());
        if (byEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = userMapper.map(createUserRequestDto);
        user.setPassword(passwordEncoder.encode(createUserRequestDto.getPassword()));
        user.setType(Type.USER);
        userService.addUser(user);
        return ResponseEntity.ok(userMapper.mapToDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getAllById(@PathVariable("id") int id) {
        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()) {
            UserDto userDto = userMapper.mapToDto(byId.get());
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id) {
        if (userService.existsById(id)) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") int id, @RequestBody User user,
                                       @AuthenticationPrincipal CurrentUser currentUser) {
        Optional<User> byId = userService.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (byId.get().getId() != currentUser.getUser().getId()){
            return ResponseEntity.notFound().build();
        }

        Optional<User> byEmail = userService.findByEmail(user.getEmail());
        if (byEmail.isPresent() && byEmail.get().getId() != id) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User userFromDb = byId.get();
        if (user.getName() != null && !user.getName().isEmpty()) {
            userFromDb.setName(user.getName());
        }
        if (user.getSurname() != null && !user.getSurname().isEmpty()) {
            userFromDb.setSurname(user.getSurname());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            userFromDb.setEmail(user.getEmail());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userFromDb.setPassword(user.getPassword());
            userFromDb.setPassword(passwordEncoder.encode(userFromDb.getPassword()));
        }
            userFromDb.setType(Type.USER);
            return ResponseEntity.ok(userRepository.save(userFromDb));
    }
}