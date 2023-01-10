package com.example.to_do_list.config;

import com.example.to_do_list.domain.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UsersService;
    private final OAuth2AuthenticationSuccessHandler authenticationSuccessHandler;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2/**", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/css/**","/images/**","/js/**","/h2/**").permitAll()
                .antMatchers("/api/team/list/**", "/api/login").permitAll()
                .antMatchers("/api/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and().logout()
                .logoutSuccessUrl("/api/team/list?page=1&size=10")
                ;
        http.formLogin().disable()
                .oauth2Login()
                .loginPage("/api/login")
                .authorizationEndpoint()
                .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UsersService)
                .and()
                .successHandler(authenticationSuccessHandler)
                .defaultSuccessUrl("/api/team/list?page=1&size=10");

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
