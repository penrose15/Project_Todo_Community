package com.example.to_do_list.common.security.filter;

import com.example.to_do_list.common.redis.redisTemplateRepository;
import com.example.to_do_list.common.security.jwt.JwtTokenizer;
import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.common.security.utils.CustomAuthorityUtils;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.repository.UsersRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final UsersRepository usersRepository;
    private final redisTemplateRepository redisTemplateRepository;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);
        }  catch (ExpiredJwtException | SignatureException ee) {
            ee.printStackTrace();
            reissueToken(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request,response);
    }

    @Override //ㅌ특정 조건에 부합하면 해당 필터의 동작을 수행하지 않고 다음 필터로 넘어간다.
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");
        return authorization == null || !authorization.startsWith("Bearer ");

    }
    private String getAccessJwtToken(HttpServletRequest request) {
        if(request.getHeader("Authorization") != null) {
            return request.getHeader("Authorization").substring(7);
        }
        return null;
    }
    private String getRefreshToken(HttpServletRequest request) {
        if(request.getHeader("Refresh") != null) {
            return request.getHeader("Refresh");
        }
        return null;
    }

    private void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(">>reissue start");
        String jws = getAccessJwtToken(request);
        String refreshToken = getRefreshToken(request);
        if(!jwtTokenizer.validateToken(jws) && refreshToken != null) {
            try {
                refreshToken = refreshToken.substring(7);
                if(jwtTokenizer.validateToken(refreshToken)) {
                    String email = jwtTokenizer.getEmailFromRefreshToken(refreshToken);
                    Users users = usersRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("잘못된 리프레시토큰"));
                    System.out.println(users.getEmail());

                    Long usersId = users.getUsersId();
                    Long usersId2 = redisTemplateRepository.findById(refreshToken)
                            .orElseThrow(() -> new NoSuchElementException("잘못된 토큰"))
                            .getUsersId();

                    if(Objects.equals(usersId2, usersId)) {
                        System.out.println(">>>" + Objects.equals(usersId2, usersId));

                        Map<String, Object> claims = new HashMap<>();
                        claims.put("username", users.getEmail());
                        claims.put("roles",users.getRole());
                        System.out.println(">>");
                        Date expiration = jwtTokenizer.getTokenExpiration(Integer.parseInt(jwtTokenizer.getAccessTokenExpirationMinutes()));
                        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
                        String accessToken = jwtTokenizer.generateAccessToken(claims, email, expiration, base64EncodedSecretKey);
                        response.setHeader("Authorization", accessToken);
                        setAuthenticationToContext(claims);
                    } else {
                        throw new MalformedJwtException("wrong refreshToken");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("리프레시 토큰 없음, 유효하지 않은 액서스 토큰");
        }
    }

    private Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws = request.getHeader("Authorization").replace("Bearer ", ""); // (3-1)
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()); // (3-2)
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();   // (3-3)

        return claims;
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("username");
        Users users = usersRepository.findByEmail(username).orElseThrow(() -> new NoSuchElementException());
        CustomUserDetails customUserDetails = new CustomUserDetails(users.getUsersId(), users.getEmail(), users.getPassword(),users.getRole());


        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
