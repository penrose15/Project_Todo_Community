package com.example.to_do_list.dto.category;

import com.example.to_do_list.domain.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategorySaveDto {
    private String name;
    private String explanation;

    @Builder
    public CategorySaveDto(String name, String explanation) {
        this.name = name;
        this.explanation = explanation;
    }

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .explanation(explanation)
                .build();
    }
}
