package com.example.to_do_list.dto.password;

import lombok.Getter;

@Getter
public class ChangePasswordDto {

    private String password;
    private String newPassword1;
    private String newPassword2;
}
