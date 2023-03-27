package com.example.to_do_list.dto.todo;

import com.example.to_do_list.domain.Todo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Validated
@NoArgsConstructor
public class TodoSaveDto {
    @NotBlank
    private String title;
    private String content;
    @NotBlank
    private String expose;

    private int priority;
    private String endDate;
    @Builder
    public TodoSaveDto(String title, String content, String expose,Integer priority, String endDate) {
        if(expose == null) {
            this.expose = "PUBLIC";
        } else {
            this.expose = expose;
        }
        if(priority == null) {
            this.priority = 4;
        } else {
            this.priority = priority;
        }
        if(endDate == null) {
            this.endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            this.endDate = endDate;
        }

        this.title = title;
        this.content = content;
    }
    public Todo toEntity() {
        return Todo.builder()
                .title(title)
                .content(content)
                .status(false)
                .expose(expose)
                .priority(priority)
                .endDate(LocalDate.parse(endDate)) // 만약 endDate가 null이면 당일로 설정
                .build();
    }

}
