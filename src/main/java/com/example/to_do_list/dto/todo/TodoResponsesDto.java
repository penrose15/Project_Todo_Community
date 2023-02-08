package com.example.to_do_list.dto.todo;

import lombok.Getter;
import lombok.NoArgsConstructor;

//todo : enddate 추가하기
@Getter
@NoArgsConstructor
public class TodoResponsesDto {
    private long id;
    private String title;
    private boolean status;



    public TodoResponsesDto(long id,String title, boolean status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }
}
