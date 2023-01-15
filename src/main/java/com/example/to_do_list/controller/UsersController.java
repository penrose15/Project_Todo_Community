package com.example.to_do_list.controller;

//import com.example.to_do_list.config.CustomUserDetails;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.user.UsersJoinDto;
import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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

    @PatchMapping("/team")
    public ResponseEntity<Long> joinTeam(@RequestBody UsersJoinDto dto) {
        Long teamId = usersService.joinTeam(dto.getTeamId(), dto.getUsersId());

        return new ResponseEntity<>(teamId, HttpStatus.OK);
    }

    @PatchMapping("/withdrawal")
    public ResponseEntity<Void> withdrawalTeam() {
        Long usersId = 1L;
        usersService.resignTeam(usersId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
