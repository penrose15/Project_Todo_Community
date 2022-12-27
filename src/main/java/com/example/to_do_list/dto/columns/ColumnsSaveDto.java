package com.example.to_do_list.dto.columns;

import com.example.to_do_list.domain.Columns;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ColumnsSaveDto {
    private String title;
    private String content;
    private boolean expose;

    @Builder
    public ColumnsSaveDto(String title, String content, boolean expose) {
        this.title = title;
        this.content = content;
        this.expose = expose;
    }

    public Columns toEntity() {
        return Columns.builder()
                .title(title)
                .content(content)
                .done(false)
                .expose(expose)
                .build();
    }
}
