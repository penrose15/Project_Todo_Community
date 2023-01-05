package com.example.to_do_list.dto.team;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TeamResponseDto {
    private long id;
    private String title;
    private String explanation;

    @Builder
    public TeamResponseDto(long id, String title, String explanation) {
        this.id = id;
        this.title = title;
        this.explanation = explanation;
    }
}
