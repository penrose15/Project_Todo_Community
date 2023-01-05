package com.example.to_do_list.dto.todo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TodoTitleResponsesDto {
    private Long todoId;
    private String title;

    @Builder
    public TodoTitleResponsesDto(Long todoId, String title) {
        this.todoId = todoId;
        this.title = title;
    }
}
