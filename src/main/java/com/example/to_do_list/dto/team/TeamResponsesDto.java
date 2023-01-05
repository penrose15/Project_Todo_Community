package com.example.to_do_list.dto.team;

import lombok.Getter;

@Getter
public class TeamResponsesDto {
    private long id;
    private String title;
    private String explanation;
    private int limit;

    public TeamResponsesDto(long id, String title, String explanation, int limit) {
        this.id = id;
        this.title = title;
        this.explanation = explanation;
        this.limit = limit;
    }
}
