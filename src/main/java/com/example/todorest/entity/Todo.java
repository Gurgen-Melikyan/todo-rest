package com.example.todorest.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "todo")
@Data
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @ManyToOne
    private Category category;
    @ManyToOne
    private User user;
}
