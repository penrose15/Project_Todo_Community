package com.example.to_do_list.service;

import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.domain.role.Role;
import com.example.to_do_list.dto.team.TeamSaveDto;
import com.example.to_do_list.dto.team.TeamUpdateDto;
import com.example.to_do_list.repository.TeamRepository;
import com.example.to_do_list.repository.TodoRepository;
import com.example.to_do_list.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Spy
    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private TodoRepository todoRepository;

    @Test
    void Team_Save() {
        TeamSaveDto teamSaveDto = TeamSaveDto.builder()
                .title("title")
                .explanation("explanation")
                .limit(5)
                .build();
        Long fakeTeamId = 1L;
        Team team = teamSaveDto.toEntity();
        Users users = Users.builder()
                .username("name")
                .email("name@gmail.com")
                .profile("dog.jpg")
                .role(Role.USER)
                .build();
        Long fakeUsersId = 1L;

        doReturn(users)
                .when(usersRepository).save(any(Users.class));
        doReturn(Optional.ofNullable(users))
                .when(usersRepository).findById(anyLong());
        doReturn(team)
                .when(teamRepository).save(any(Team.class));

        Long newTeamId= teamService.save(teamSaveDto, fakeUsersId);
        System.out.println(newTeamId);
        System.out.println(team.getUsersList());
//        assertThat(newTeamId).isEqualTo(fakeTeamId);
        assertThat(team.getTitle()).isEqualTo("title");
        assertThat(team.getUsersList()).contains(users);
    }

    @Test
    void Team_Update() {
        TeamSaveDto teamSaveDto = TeamSaveDto.builder()
                .title("title")
                .explanation("explanation")
                .limit(5)
                .build();
        Long fakeTeamId = 1L;
        Team team = teamSaveDto.toEntity();
        TeamUpdateDto teamUpdateDto = TeamUpdateDto.builder()
                .title("title2")
                .explanation("explanation2")
                .build();
        Users users = Users.builder()
                .username("name")
                .email("name@gmail.com")
                .profile("dog.jpg")
                .role(Role.USER)
                .build();
        Long fakeUsersId = 1L;

        doReturn(Optional.ofNullable(users))
                .when(usersRepository).findById(anyLong());
        doReturn(Optional.ofNullable(team))
                .when(teamRepository).findById(anyLong());

        Long newTeamId= teamService.update(teamUpdateDto, fakeTeamId, fakeUsersId);
        assertThat(team.getTitle()).isEqualTo("title2");
    }

    @Test
    void Team_MandateHost() {
        TeamSaveDto teamSaveDto = TeamSaveDto.builder()
                .title("title")
                .explanation("explanation")
                .limit(5)
                .build();
        Long fakeTeamId = 1L;
        Team team = teamSaveDto.toEntity();
        team.setHostUserId(1L);

        Users users = Users.builder()
                .username("name")
                .email("name@gmail.com")
                .profile("dog.jpg")
                .role(Role.USER)
                .build();
        Long fakeUsersId = 1L;
        Users users1 = Users.builder()
                .username("name")
                .email("name@gmail.com")
                .profile("dog.jpg")
                .role(Role.USER)
                .build();
        Long fakeUsersId1 = 2L;

        doReturn(Optional.ofNullable(team))
                .when(teamRepository).findById(anyLong());
        doNothing()
                .when(teamService).verifyingUsers(anyList(), anyLong());
        doReturn(Optional.ofNullable(users))
                .when(usersRepository).findById(anyLong());
        Long newHostsId = teamService.mandateHost(fakeTeamId, fakeUsersId, fakeUsersId1);

        assertThat(newHostsId).isEqualTo(fakeUsersId1);
    }
}
