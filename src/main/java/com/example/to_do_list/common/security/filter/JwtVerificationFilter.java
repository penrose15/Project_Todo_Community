package com.example.to_do_list.common.security.filter;

import com.example.to_do_list.common.security.jwt.JwtTokenizer;
import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.common.security.utils.CustomAuthorityUtils;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.repository.UsersRepository;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final UsersRepository usersRepository;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);
        } catch (SignatureException se) {
            se.printStackTrace();
            request.setAttribute("exception", se);
        } catch (ExpiredJwtException ee) {
            ee.printStackTrace();
            request.setAttribute("exception", ee);
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

    private Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws = request.getHeader("Authorization").replace("Bearer ", ""); // (3-1)
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()); // (3-2)
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();   // (3-3)

        return claims;
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("username");
        Users users = usersRepository.findByEmail(username).orElseThrow(() -> new NoSuchElementException());
        CustomUserDetails customUserDetails = new CustomUserDetails(users);


        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
