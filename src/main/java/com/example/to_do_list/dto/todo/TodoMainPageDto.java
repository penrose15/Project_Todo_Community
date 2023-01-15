package com.example.to_do_list.dto.todo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class TodoMainPageDto {
    private List<TodoResponsesDto> todoResponsesDto;
    private String date;
    private int percentage;

    @Builder
    public TodoMainPageDto(List<TodoResponsesDto> todoResponsesDto, String date, int percentage) {
        this.todoResponsesDto = todoResponsesDto;
        this.date = date;
        this.percentage = percentage;
    }
}
