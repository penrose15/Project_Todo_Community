package com.example.to_do_list.dto;

import lombok.Getter;

@Getter
public class AuthCodeDto {

    private String email;

    private String authCode;
}
