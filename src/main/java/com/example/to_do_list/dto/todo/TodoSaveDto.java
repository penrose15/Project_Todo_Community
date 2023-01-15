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
    private String expose;
    private String endDate;
    @Builder
    public TodoSaveDto(String title, String content, String expose, String endDate) {
        this.title = title;
        this.content = content;
        this.expose = expose;
        this.endDate = endDate;
    }
    public Todo toEntity() {
        return Todo.builder()
                .title(title)
                .content(content)
                .status(false)
                .expose(expose)
                .endDate(LocalDate.parse(endDate))
                .build();
    }
}
