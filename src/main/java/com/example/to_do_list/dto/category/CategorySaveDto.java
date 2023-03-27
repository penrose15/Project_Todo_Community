package com.example.to_do_list.dto.category;

import com.example.to_do_list.domain.Category;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@Getter
public class CategorySaveDto {
    @NotBlank
    private Long usersId;
    @NotBlank
    private String name;
    private String explanation;

    @Builder
    public CategorySaveDto(String name, String explanation) {
        this.name = name;
        this.explanation = explanation;
    }

    public Category toEntity() {
        return Category.builder()
                .usersId(usersId)
                .name(name)
                .explanation(explanation)
                .build();
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }
}
