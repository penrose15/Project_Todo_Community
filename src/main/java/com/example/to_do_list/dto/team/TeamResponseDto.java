package com.example.to_do_list.dto.team;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamResponseDto {
    private long teamId;
    private String title;
    private String explanation;

    @Builder
    public TeamResponseDto(long teamId, String title, String explanation) {
        this.teamId = teamId;
        this.title = title;
        this.explanation = explanation;
    }
}
