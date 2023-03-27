package com.example.to_do_list.dto.team;

import com.example.to_do_list.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@Validated
@Getter
@NoArgsConstructor
public class TeamSaveDto {
    @NotBlank
    private String title;
    private String explanation;
    @Min(1)
    @Max(30)
    private int limit;
    @Min(0)
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
