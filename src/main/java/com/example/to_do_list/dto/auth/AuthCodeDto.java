package com.example.to_do_list.dto.auth;

import lombok.Getter;

@Getter
public class AuthCodeDto {

    private String email;

    private String authCode;
}
