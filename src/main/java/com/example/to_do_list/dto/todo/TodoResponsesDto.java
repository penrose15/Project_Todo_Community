package com.example.to_do_list.dto.todo;

import lombok.Getter;

@Getter
public class TodoResponsesDto {
    private String title;
    private boolean done;


    public TodoResponsesDto(String title, boolean done) {
        this.title = title;
        this.done = done;
    }
}
