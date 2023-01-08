package com.example.to_do_list.config.dto;

import com.example.to_do_list.domain.Users;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String username;
    private String email;
    private String profile;

    public SessionUser(Users users) {
        this.username = users.getUsername();
        this.email = users.getEmail();
        this.profile = users.getProfile();
    }
}
