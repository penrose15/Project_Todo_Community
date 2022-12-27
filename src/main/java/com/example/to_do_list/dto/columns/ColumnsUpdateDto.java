package com.example.to_do_list.dto.columns;

import com.example.to_do_list.domain.Columns;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ColumnsUpdateDto {
    private String title;
    private String content;
    private boolean done;
    private boolean expose;

    @Builder
    public ColumnsUpdateDto(String title, String content, boolean done, boolean expose) {
        this.title = title;
        this.content = content;
        this.done = done;
        this.expose = expose;
    }

    public Columns toEntity() {
        return Columns.builder()
                .title(title)
                .content(content)
                .done(done)
                .expose(expose)
                .build();
    }
}
