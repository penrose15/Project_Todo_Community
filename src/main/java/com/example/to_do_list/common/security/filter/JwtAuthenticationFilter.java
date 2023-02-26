package com.example.to_do_list.common.security.filter;

import com.example.to_do_list.common.redis.redisTemplateRepository;
import com.example.to_do_list.common.security.dto.LoginDto;
import com.example.to_do_list.common.security.jwt.JwtTokenizer;
import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.domain.RefreshToken;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    private final redisTemplateRepository redisTemplateRepository;
    private final JwtTokenizer jwtTokenizer;

    private final PasswordEncoder passwordEncoder;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        Users users = usersRepository.findByEmail(loginDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일"));

        if(!passwordEncoder.matches(loginDto.getPassword(), users.getPassword())) {
            throw new AuthenticationException("wrong password") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();


        String accessToken = delegateAccessToken(customUserDetails);
        String refreshToken = delegateRefreshToken(customUserDetails);

        response.setHeader("Authorization","Bearer "+ accessToken);
        response.setHeader("Refresh", "Bearer "+ refreshToken);

        redisTemplateRepository.save(new RefreshToken("Bearer" + refreshToken, customUserDetails.getUsersId()));

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    private String delegateAccessToken(CustomUserDetails customUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", customUserDetails.getEmail());
        claims.put("usersId", customUserDetails.getUsersId());
        claims.put("roles",customUserDetails.getRoles());

        String subject = customUserDetails.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(Integer.parseInt(jwtTokenizer.getAccessTokenExpirationMinutes()));
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
        return accessToken;
    }

    private String delegateRefreshToken(CustomUserDetails customUserDetails) {
        String subject = customUserDetails.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(Integer.parseInt(jwtTokenizer.getRefreshTokenExpirationMinutes()));
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
        return refreshToken;
    }


}
