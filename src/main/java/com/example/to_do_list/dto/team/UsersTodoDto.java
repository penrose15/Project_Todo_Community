package com.example.to_do_list.dto.team;

import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.dto.todo.TodoTitleResponsesDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UsersTodoDto {

    private Long usersId;
    private String username;
    private List<TodoTitleResponsesDto> todoList;

    @Builder
    public UsersTodoDto(Long usersId, String username, List<TodoTitleResponsesDto> todoList) {
        this.usersId = usersId;
        this.username = username;
        this.todoList = todoList;
    }
}
