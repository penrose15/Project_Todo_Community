package com.example.to_do_list.config;

import com.example.to_do_list.domain.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UsersService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/css/**","/images/**","/js/**","/h2/**").permitAll()
                .antMatchers("/api/team/list/**").permitAll()
                .antMatchers("/api/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and().logout()
                .logoutSuccessUrl("/api/team/list?page=1&size=10")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UsersService)
                .and()
                .defaultSuccessUrl("/api/team/list?page=1&size=10");
        return http.build();
    }
}
