package com.example.to_do_list.dto.todo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoCalendarDTO {
    private Long id;
    private String title;
    private String endDate;
    private int priority;

    @Builder
    public TodoCalendarDTO(Long id, String title, String endDate, int priority) {
        this.id = id;
        this.title = title;
        this.endDate = endDate;
        this.priority = priority;
    }
}
