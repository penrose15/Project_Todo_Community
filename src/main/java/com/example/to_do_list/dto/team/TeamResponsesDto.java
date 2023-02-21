package com.example.to_do_list.dto.team;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamResponsesDto {
    private long teamId;
    private String title;
    private String explanation;
    private int limits;
    private int criteria;

    public TeamResponsesDto(long teamId, String title, String explanation, int limits, int criteria) {
        this.teamId = teamId;
        this.title = title;
        this.explanation = explanation;
        this.limits = limits;
        this.criteria = criteria;
    }
}
