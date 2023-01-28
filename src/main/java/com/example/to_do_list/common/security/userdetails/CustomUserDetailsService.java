package com.example.to_do_list.common.security.userdetails;

import com.example.to_do_list.common.security.utils.CustomAuthorityUtils;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.repository.UsersRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component//authenticationmanager가 customuUserDetailService에게 사용자의 UserDetails 조회 위임
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final CustomAuthorityUtils authorityUtils;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> usersOptional = usersRepository.findByEmail(username);
        Users users = usersOptional.orElseThrow(() -> new NoSuchElementException(" 존재하지 않는 멤버"));

        return new CustomUserDetails(users.getUsersId(), users.getEmail(), users.getRole());
    }

}
