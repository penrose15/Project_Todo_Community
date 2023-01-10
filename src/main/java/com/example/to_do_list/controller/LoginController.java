package com.example.to_do_list.controller;

import com.example.to_do_list.config.CustomUserDetails;
import com.example.to_do_list.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLoginPage(@AuthenticationPrincipal CustomUserDetails user,
                               Model model) {
        if(user != null) {
            model.addAttribute("username",user.getName());
        }

        return "auth/oauth-login";
    }
}

