package com.example.to_do_list.dto;

import com.example.to_do_list.dto.todo.TodoCalendarDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CalendarDto {
    private int year;
    private int month;
    private List<TodoCalendarDTO> todoCalendarDTOS;

    @Builder
    public CalendarDto(int year, int month, List<TodoCalendarDTO> todoCalendarDTOS) {
        this.year = year;
        this.month = month;
        this.todoCalendarDTOS = todoCalendarDTOS;
    }
}
