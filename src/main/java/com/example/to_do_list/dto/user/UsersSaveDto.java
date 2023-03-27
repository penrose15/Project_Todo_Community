package com.example.to_do_list.dto.user;

import com.example.to_do_list.domain.Users;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Validated
public class UsersSaveDto {
    @Email(message = "유효한 이메일 형식이어야 합니다.", regexp = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")
    @NotBlank
    private String password;
    private List<String> roles;
    private Boolean checkedAuthCode;

    public UsersSaveDto(String email, String password, Boolean checkedAuthCode) {
        this.email = email;
        this.password = password;
        this.checkedAuthCode = checkedAuthCode;
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
