package com.example.to_do_list.controller;

//import com.example.to_do_list.config.security.CustomUserDetails;

import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.user.UsersJoinDto;
import com.example.to_do_list.dto.user.UsersSaveDto;
import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UsersController {
    private final UsersService usersService;

//    @GetMapping("/api/me")
//    @PreAuthorize("hasRole('USER')")
//    public Long get(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        return usersService.findById(userDetails.getId());
//    }
    @PostMapping("/tmp")
    public Long tmpUser() {
        return usersService.save();
    }

    @PostMapping("/account")
    public ResponseEntity<String> joinUser(@RequestBody UsersSaveDto usersSaveDto) {
        String email = usersService.createUsers(usersSaveDto);

        return new ResponseEntity<>(email, HttpStatus.CREATED);
    }

    @PatchMapping("/team/{teamId}")
    public ResponseEntity<Long> joinTeam(@PathVariable(value = "teamId") Long teamId,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getEmail();
        Long id = usersService.joinTeam(teamId, email);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PatchMapping("/withdrawal")//
    public ResponseEntity<Void> withdrawalTeam(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getEmail();
        usersService.resignTeam(email);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
