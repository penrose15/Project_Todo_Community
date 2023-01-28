package com.example.to_do_list.dto.todo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TodoIdsDto {
    List<Long> ids;

    public TodoIdsDto(List<Long> ids) {
        this.ids = ids;
    }
}
