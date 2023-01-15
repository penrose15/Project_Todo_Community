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
    private int criteria;

    @Builder
    public TeamSaveDto(String title, String explanation, int limit, int criteria) {
        this.title = title;
        this.explanation = explanation;
        this.limit = limit;
        this.criteria = criteria;
    }

    public Team toEntity() {
        return Team.builder()
                .title(title)
                .explanation(explanation)
                .limits(limit)
                .criteria(criteria)
                .build();
    }
}
