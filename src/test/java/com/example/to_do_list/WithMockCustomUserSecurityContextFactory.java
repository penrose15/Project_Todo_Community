package com.example.to_do_list;

import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {



    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        CustomUserDetails principal =
                new CustomUserDetails(annotation.usersId(), annotation.email(), List.of(annotation.roles()));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(annotation.roles()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}
