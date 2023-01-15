package com.example.to_do_list.controller;

//import com.example.to_do_list.config.CustomUserDetails;
import com.example.to_do_list.dto.team.*;
import com.example.to_do_list.service.TeamService;
import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final UsersService usersService;

    @PostMapping("/")
    public ResponseEntity<Long> createTeam(@RequestBody TeamSaveDto request
//                                           @AuthenticationPrincipal CustomUserDetails user
    ) {
//        Long usersId = user.getId();
        Long usersId = 1L;
        Long result = teamService.save(request, usersId);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Long> updateTeam(@PathVariable(value = "id") Long teamId,
                           @RequestBody TeamUpdateDto request
//                           @AuthenticationPrincipal CustomUserDetails user
    ) {
//        Long usersId = user.getId();
        Long usersId = 1L;
        Long result =  teamService.update(request, teamId, usersId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}") //팀원도 조회 가능하도록 변경?하기
    public ResponseEntity<TeamResponseDto> showTeamDetails(@PathVariable(value = "id") Long teamId) {
        TeamResponseDto response = teamService.showTeamDetails(teamId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<TeamResponsesDto>> showTeamList(@RequestParam(value = "page") int page,
                                                               @RequestParam(value = "size") int size) {
        Slice<TeamResponsesDto> teamSlice = teamService.teamList(page, size);
        List<TeamResponsesDto> teamList = teamSlice.getContent();

        return new ResponseEntity<>(teamList, HttpStatus.OK);
    }

    @GetMapping("/todoList/{id}/{date}") // 방장의 todolist가 조회 안됨
    public ResponseEntity<TeamDetailResponseDto> showUsersTodoList(@PathVariable(value = "id") Long teamId,
                                                                   @PathVariable(value = "date") String date
//                                                                   @AuthenticationPrincipal CustomUserDetails user
    ) {
        usersService.findById(1L);

        LocalDate localDate = LocalDate.parse(date);
        TeamDetailResponseDto response = teamService.showUsersTodoList(teamId, localDate);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/host") //deserialize error
    public ResponseEntity<Long> mandateHost(@RequestParam(value = "teamId") Long teamId,
                                            @RequestBody Long usersId
//                                            @AuthenticationPrincipal CustomUserDetails users
    ) {
        Long hostsId = 1L;
        Long response = teamService.mandateHost(teamId, hostsId, usersId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteUser(@RequestParam(value = "teamId") Long teamId,
                                           @RequestParam(value = "usersId") Long usersId
//                                           @AuthenticationPrincipal CustomUserDetails users
    ) {
        Long hostsId = 1L;
        teamService.deleteUser(teamId, hostsId, usersId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
