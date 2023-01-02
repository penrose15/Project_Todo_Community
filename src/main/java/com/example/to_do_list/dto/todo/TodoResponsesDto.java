package com.example.to_do_list.dto.todo;

import lombok.Getter;

@Getter
public class TodoResponsesDto {
    private String title;
    private boolean status;


    public TodoResponsesDto(String title, boolean status) {
        this.title = title;
        this.status = status;
    }
}
