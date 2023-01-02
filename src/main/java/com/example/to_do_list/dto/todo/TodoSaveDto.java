package com.example.to_do_list.dto.todo;

import com.example.to_do_list.domain.Todo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TodoSaveDto {
    private String title;
    private String content;
    private boolean expose;
    private LocalDate endDate;
    @Builder
    public TodoSaveDto(String title, String content, boolean expose, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.expose = expose;
        this.endDate = endDate;
    }
    public Todo toEntity() {
        return Todo.builder()
                .title(title)
                .content(content)
                .done(false)
                .expose(expose)
                .endDate(endDate)
                .build();
    }
}
