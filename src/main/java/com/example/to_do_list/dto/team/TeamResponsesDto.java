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

    public TeamResponsesDto(long teamId, String title, String explanation, int limits) {
        this.teamId = teamId;
        this.title = title;
        this.explanation = explanation;
        this.limits = limits;
    }
}
