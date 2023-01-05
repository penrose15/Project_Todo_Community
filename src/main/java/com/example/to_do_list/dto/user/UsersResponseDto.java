package com.example.to_do_list.dto.user;

import lombok.Getter;

@Getter
public class UsersResponseDto {
    private long id;
    private String nickname;
    private String profile;


    public UsersResponseDto(long id, String nickname, String profile) {
        this.id = id;
        this.nickname = nickname;
        this.profile = profile;
    }
}
