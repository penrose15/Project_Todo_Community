package com.example.to_do_list.dto.todo;

import com.example.to_do_list.domain.Todo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TodoUpdateDto {
    private String title;
    private String content;
    private boolean status;
    private String expose;
    private String endDate;

    @Builder
    public TodoUpdateDto(String title, String content, boolean status, String expose, String endDate) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.expose = expose;
        this.endDate = endDate;
    }


    public Todo toEntity() {
        LocalDate localDate = LocalDate.parse(this.endDate);
        return Todo.builder()
                .title(title)
                .content(content)
                .status(status)
                .expose(expose)
                .endDate(localDate)
                .build();
    }
}
