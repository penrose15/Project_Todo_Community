package com.example.to_do_list.dto.todo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TodoTitleResponsesDto {
    private Long id;
    private String title;

    public TodoTitleResponsesDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
