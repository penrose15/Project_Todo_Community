package com.example.to_do_list.dto.todo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TodoResponseDto {
    private String title;
    private String content;
    private boolean done;
    private boolean expose;
    private LocalDate endDate;


    @Builder
    public TodoResponseDto(String title, String content, boolean done, boolean expose, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.done = done;
        this.expose = expose;
        this.endDate = endDate;
    }
}
