package com.example.to_do_list.dto.team;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TeamUpdateDto {
    private String title;
    private String explanation;

    @Builder
    public TeamUpdateDto(String title, String explanation) {
        this.title = title;
        this.explanation = explanation;
    }
}
