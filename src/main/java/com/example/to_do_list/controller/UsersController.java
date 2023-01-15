package com.example.to_do_list.controller;

//import com.example.to_do_list.config.CustomUserDetails;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class UsersController {

    private final HttpSession httpSession;
    private final UsersService usersService;

//    @GetMapping("/api/me")
//    @PreAuthorize("hasRole('USER')")
//    public Long get(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        return usersService.findById(userDetails.getId());
//    }
    @PostMapping("/api/tempuser")
    public Long tmpUser() {
        return usersService.save();
    }
}
