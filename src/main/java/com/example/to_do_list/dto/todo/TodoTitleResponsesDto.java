package com.example.to_do_list.dto.todo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TodoTitleResponsesDto {
    private Long id;
    private String title;
    private boolean status;

    public TodoTitleResponsesDto(Long id, String title, boolean status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }
}
