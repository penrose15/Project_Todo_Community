package com.example.to_do_list.dto.team;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamResponseDto {
    private long teamId;
    private String title;
    private String explanation;
    private int limit;
    private int criteria;

    @Builder
    public TeamResponseDto(long teamId, String title, String explanation, int limit, int criteria) {
        this.teamId = teamId;
        this.title = title;
        this.explanation = explanation;
        this.limit = limit;
        this.criteria = criteria;

    }
}
