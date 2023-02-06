package com.example.to_do_list.dto.category;

import com.example.to_do_list.dto.todo.TodoResponsesDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CategoriesResponseDto {
    public String name;
    public List<TodoResponsesDto> todoResponsesDtos;

    @Builder
    public CategoriesResponseDto(String name, List<TodoResponsesDto> todoResponsesDtos) {
        this.name = name;
        this.todoResponsesDtos = todoResponsesDtos;
    }
}
