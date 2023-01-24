//package com.example.to_do_list.controller;
//
//import com.example.to_do_list.config.security.*;
//import com.example.to_do_list.domain.role.Role;
//import com.example.to_do_list.service.AuthService;
//import io.jsonwebtoken.Claims;
//import lombok.RequiredArgsConstructor;
//import org.apache.tomcat.util.http.HeaderUtil;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/auth")
//public class AuthController {
//    private final AuthService authService;
//
//    @PostMapping("/refresh")
//    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response, @RequestBody String accessToken) {
//        return ResponseEntity.ok().body(authService.refreshToken(request, response, accessToken));
//    }
//}
