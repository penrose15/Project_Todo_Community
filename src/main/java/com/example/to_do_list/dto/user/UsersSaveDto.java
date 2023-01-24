package com.example.to_do_list.dto.user;

import com.example.to_do_list.domain.Users;
import lombok.Getter;

import java.util.List;

@Getter
public class UsersSaveDto {
    private String email;
    private String password;
    private List<String> roles;

    public UsersSaveDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Users toEntity() {
        return new Users(this.email, this.password, this.roles);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
