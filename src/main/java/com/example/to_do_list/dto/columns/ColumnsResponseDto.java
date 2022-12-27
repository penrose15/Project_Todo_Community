package com.example.to_do_list.dto.columns;

import com.example.to_do_list.domain.Columns;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ColumnsResponseDto {
    private String title;
    private String content;
    private boolean done;
    private boolean expose;


    @Builder
    public ColumnsResponseDto(String title, String content, boolean done, boolean expose) {
        this.title = title;
        this.content = content;
        this.done = done;
        this.expose = expose;
    }
}
