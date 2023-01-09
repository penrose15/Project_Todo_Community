package com.example.to_do_list.controller;

import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class UsersController {

    private final HttpSession httpSession;
    private final UsersService usersService;
}
