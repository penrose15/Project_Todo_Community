package com.example.to_do_list.dto.todo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TodoResponseDto {
    private String title;
    private String content;
    private boolean status;
    private boolean expose;
    private LocalDate endDate;


    @Builder
    public TodoResponseDto(String title, String content, boolean status, boolean expose, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.expose = expose;
        this.endDate = endDate;
    }
}
