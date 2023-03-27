package com.example.to_do_list.service;

import com.example.to_do_list.common.exception.BusinessLogicException;
import com.example.to_do_list.common.exception.ExceptionCode;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.password.ChangePasswordDto;
import com.example.to_do_list.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ChangePasswordService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public Boolean changePassword(ChangePasswordDto dto, String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        boolean checkPassword = passwordEncoder.matches(dto.getPassword(),users.getPassword());
        if(!checkPassword) return false;

        if(!Objects.equals(dto.getNewPassword1(), dto.getNewPassword2())) {
            throw new BusinessLogicException(ExceptionCode.PASSWORD_NOt_MATCH);
        }

        users.setPassword(passwordEncoder.encode(dto.getNewPassword2()));

        usersRepository.save(users);
        return true;
    }
}
