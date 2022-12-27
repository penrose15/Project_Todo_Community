package com.example.to_do_list.dto.columns;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ColumnsResponsesDto {
    private String title;
    private boolean done;

    @Builder
    public ColumnsResponsesDto(String title, boolean done) {
        this.title = title;
        this.done = done;
    }
}
