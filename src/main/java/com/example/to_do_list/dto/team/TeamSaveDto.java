package com.example.to_do_list.dto.team;

import com.example.to_do_list.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class TeamSaveDto {
    private String title;
    private String explanation;
    private int limit;

    @Builder
    public TeamSaveDto(String title, String explanation, int limit) {
        this.title = title;
        this.explanation = explanation;
        this.limit = limit;
    }

    public Team toEntity() {
        return Team.builder()
                .title(title)
                .explanation(explanation)
                .limit(limit)
                .usersIdList(new ArrayList<>())
                .build();
    }
}
