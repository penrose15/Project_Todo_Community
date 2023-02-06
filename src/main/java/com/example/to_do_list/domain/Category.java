package com.example.to_do_list.domain;

import com.example.to_do_list.dto.category.CategoryUpdateDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column
    private String explanation;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Todo> todoList = new ArrayList<>();

    @Builder
    public Category(String name, String explanation) {
        this.name = name;
        this.explanation = explanation;
    }

    public void update(CategoryUpdateDto updateDto) {
        if(updateDto.getName() != null) {
            this.name = updateDto.getName();
        }
        if(updateDto.getExplanation() != null) {
            this.explanation = updateDto.getExplanation();
        }
    }

    public void addEmptyList() {
        if(this.todoList == null) {
            this.todoList = new ArrayList<>();
        }
    }

    public void addTodo(Todo todo) {
        if(this.todoList != null && !todoList.contains(todo)) {
            this.todoList.add(todo);
        }
        if(this.todoList == null) {
            this.todoList = new ArrayList<>();
            this.todoList.add(todo);
        }
    }
}
