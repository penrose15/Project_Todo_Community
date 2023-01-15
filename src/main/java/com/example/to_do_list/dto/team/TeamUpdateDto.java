package com.example.to_do_list.dto.team;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TeamUpdateDto {
    private String title;
    private String explanation;
    private Integer limit;
    private Integer criteria;

    @Builder
    public TeamUpdateDto(String title, String explanation, Integer limit, Integer criteria) {
        this.title = title;
        this.explanation = explanation;
        this.limit = limit;
        this.criteria = criteria;
    }
}
