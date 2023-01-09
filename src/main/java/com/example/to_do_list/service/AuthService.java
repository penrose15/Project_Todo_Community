package com.example.to_do_list.service;

import com.example.to_do_list.config.CookieUtil;
import com.example.to_do_list.config.CustomUserDetails;
import com.example.to_do_list.config.JwtTokenProvider;
import com.example.to_do_list.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@RequiredArgsConstructor
@Service
public class AuthService {
    @Value("${app.auth.token.refresh-cookie-key}")
    private String cookieKey;

    private final UsersRepository usersRepository;
    private final JwtTokenProvider tokenProvider;

    public String refreshToken(HttpServletRequest request, HttpServletResponse response, String oldAccessToken) {
        String oldRefreshToken = CookieUtil.getCookie(request, cookieKey)
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("no Refresh Token Cookie"));
        if(!tokenProvider.validateToken(oldRefreshToken)) {
            throw new RuntimeException("not validated refresh Token");
        }

        Authentication authentication = tokenProvider.getAuthentication(oldAccessToken);
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Long id = Long.valueOf(user.getName());

        String savedToken = usersRepository.getRefreshTokenById(id);

        if(!savedToken.equals(oldRefreshToken)) {
            throw new RuntimeException("not matched refresh Token");
        }

        String accessToken = tokenProvider.createAccessToken(authentication);
        tokenProvider.createRefreshToken(authentication, response);

        return accessToken;
    }
}
