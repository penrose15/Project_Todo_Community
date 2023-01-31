package com.example.to_do_list.controller;

//import com.example.to_do_list.config.security.CustomUserDetails;

import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.MultiResponseDto;
import com.example.to_do_list.dto.SingleResponseDto;
import com.example.to_do_list.dto.team.*;
import com.example.to_do_list.dto.user.UsersMandateDto;
import com.example.to_do_list.service.TeamService;
import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final UsersService usersService;

    @PostMapping
    public ResponseEntity<Long> createTeam(@RequestBody TeamSaveDto request,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
        Long userId = usersService.findByEmail(email);
//        Long usersId = 1L;
        Long result = teamService.save(request, userId);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Long> updateTeam(@PathVariable(value = "id") Long teamId,
                           @RequestBody TeamUpdateDto request,
                           @AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
        Long usersId = usersService.findByEmail(email);
        Long result =  teamService.update(request, teamId, usersId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}") //팀원도 조회 가능하도록 변경?하기
    public ResponseEntity<TeamResponseDto> showTeamDetails(@PathVariable(value = "id") Long teamId) {
        TeamResponseDto response = teamService.showTeamDetails(teamId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity showTeamList(@RequestParam(value = "page") int page,
                                       @RequestParam(value = "size") int size) {
        Page<TeamResponsesDto> teamPage = teamService.teamList(page, size);
        List<TeamResponsesDto> teamList = teamPage.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(teamList, teamPage), HttpStatus.OK);
    }

    @GetMapping("/todoList/{id}/{date}")
    public ResponseEntity showUsersTodoList(@PathVariable(value = "id") Long teamId,
                                                                   @PathVariable(value = "date") String date,
                                                                   @AuthenticationPrincipal CustomUserDetails user) {
        usersService.findByEmail(user.getUsername());

        LocalDate localDate = LocalDate.parse(date);
        TeamDetailResponseDto response = teamService.showUsersTodoList(teamId, localDate);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PatchMapping("/host")
    public ResponseEntity mandateHost(@RequestParam(value = "teamId") Long teamId,
                                            @RequestBody UsersMandateDto dto,
                                            @AuthenticationPrincipal CustomUserDetails users) {
        String email = users.getUsername();

        Long hostsId = usersService.findByEmail(email);
        Long response = teamService.mandateHost(teamId, hostsId, dto.getUsersId());

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUser(@RequestParam(value = "teamId") Long teamId,
                                           @RequestParam(value = "usersId") Long usersId,
                                           @AuthenticationPrincipal CustomUserDetails users) {
        Long hostsId = users.getUsersId();
        teamService.deleteUser(teamId, hostsId, usersId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable(value = "id")Long id,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usersId = userDetails.getUsersId();
        teamService.deleteTeam(usersId, id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
