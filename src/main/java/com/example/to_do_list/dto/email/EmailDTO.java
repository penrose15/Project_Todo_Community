package com.example.to_do_list.dto.email;

import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class EmailDTO {
    @Email
    String email;
}
