package com.example.to_do_list.dto.todo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TodoCalendarDTO {
    private Long id;
    private String title;
    private LocalDate endDate;
    private int priority;

    @Builder
    public TodoCalendarDTO(Long id, String title, LocalDate endDate, int priority) {
        this.id = id;
        this.title = title;
        this.endDate = endDate;
        this.priority = priority;
    }
}
