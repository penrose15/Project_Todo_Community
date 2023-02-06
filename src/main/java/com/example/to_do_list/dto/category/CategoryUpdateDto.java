package com.example.to_do_list.dto.category;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryUpdateDto {
    private String name;
    private String explanation;

    @Builder
    public CategoryUpdateDto(String name, String explanation) {
        this.name = name;
        this.explanation = explanation;
    }
}
