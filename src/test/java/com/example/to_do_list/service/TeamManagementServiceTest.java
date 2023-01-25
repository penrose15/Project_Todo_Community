package com.example.to_do_list.service;

import com.example.to_do_list.domain.Attend;
import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.domain.role.Role;
import com.example.to_do_list.dto.team.TeamSaveDto;
import com.example.to_do_list.repository.AttendRepository;
import com.example.to_do_list.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TeamManagementServiceTest {
    private final static LocalDate LOCAL_DATE = LocalDate.of(2023, 2, 1);

    @Mock
    private Clock clock;

    private Clock fixedClock;

    @InjectMocks
    private TeamManagementService teamManagementService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private AttendRepository attendRepository;

    @BeforeEach
    void setClock() {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

    }

    @Test
    void deleteUser() {
        TeamSaveDto teamSaveDto1 = TeamSaveDto.builder()
                .title("title")
                .explanation("explanation")
                .limit(5)
                .build();
        Long fakeTeamId1 = 1L;
        Team team1 = teamSaveDto1.toEntity();
        TeamSaveDto teamSaveDto2 = TeamSaveDto.builder()
                .title("title")
                .explanation("explanation")
                .limit(5)
                .build();
        Long fakeTeamId2 = 2L;
        Team team2 = teamSaveDto2.toEntity();

        Users users1 = Users.builder()
                .username("name1")
                .email("name1@gmail.com")
                .role(List.of(Role.USER.getRole()))
                .build();
        long fakeUsersId1 = 1L;
        Users users2 = Users.builder()
                .username("name")
                .email("name@gmail.com")
                .role(List.of(Role.USER.getRole()))
                .build();
        long fakeUsersId2 = 2L;
        Users users3 = Users.builder()
                .username("name3")
                .email("name3@gmail.com")
                .role(List.of(Role.USER.getRole()))
                .build();
        long fakeUsersId3 = 3L;
        Users users4 = Users.builder()
                .username("name4")
                .email("name4@gmail.com")
                .role(List.of(Role.USER.getRole()))
                .build();
        long fakeUsersId4 = 4L;

        team1.addUsers(users1);
        team1.addUsers(users3);
        team1.setTeamIdForTest(1L);
        team1.setHostUserId(fakeUsersId1);

        team2.addUsers(users2);
        team2.addUsers(users4);
        team2.setTeamIdForTest(2L);
        team2.setHostUserId(fakeUsersId2);

        List<Team> team = new ArrayList<>();
        team.add(team1); team.add(team2);

        doReturn(team)
                .when(teamRepository).findAll();

        Attend attend1 = Attend.builder()
                .userId(1L)
                .team(team1)
                .date(LocalDate.now())
                .percentage(33)
                .build();
        Attend attend2 = Attend.builder()
                .userId(2L)
                .team(team1)
                .date(LocalDate.now())
                .percentage(0)
                .build();
        Attend attend3 = Attend.builder()
                .userId(3L)
                .team(team2)
                .date(LocalDate.now())
                .percentage(40)
                .build();
        Attend attend4 = Attend.builder()
                .userId(4L)
                .team(team2)
                .date(LocalDate.now())
                .percentage(0)
                .build();

        List<Attend> attendList = new ArrayList<>();
        attendList.add(attend1);
        attendList.add(attend3);

        List<Attend> attendList2 = new ArrayList<>();
        attendList2.add(attend2);
        attendList2.add(attend4);

        for (Team team3 : team) {
            doReturn(attendList)
                    .when(attendRepository).findByTeamIdAndDate(eq(team3.getTeamId()), eq(LOCAL_DATE));

            List<Users> usersList = team3.getUsersList();
            List<Long> usersIdList = new ArrayList<>();
            for (Users users : usersList) {
                usersIdList.add(users.getUsersId());
            }

            for (Long aLong : usersIdList) {
                doNothing()
                        .when(teamService).deleteUser(eq(team3.getTeamId()), eq(team3.getHostUserId()), eq(aLong));
            }
        }

//        assertThat(teamRepository.findAll().get(0).getUsersList().size()).isEqualTo(1);


    }
}
