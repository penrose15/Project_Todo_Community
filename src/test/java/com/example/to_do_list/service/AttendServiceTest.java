package com.example.to_do_list.service;

import com.example.to_do_list.domain.Attend;
import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.domain.role.Role;
import com.example.to_do_list.dto.team.TeamSaveDto;
import com.example.to_do_list.repository.AttendRepository;
import com.example.to_do_list.repository.TeamRepository;
import com.example.to_do_list.repository.todo.TodoRepository;
import com.example.to_do_list.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class AttendServiceTest {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2023, 2, 7);

    @Spy
    @InjectMocks
    private AttendService attendService;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private AttendRepository attendRepository;
    @Mock
    private TodoRepository todoRepository;
    @Mock
    private UsersRepository usersRepository;

    @Mock
    private Clock clock;

    private Clock fixedClock;


    @Test
    void getPercentage() {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        Todo todo = Todo.builder()
                .title("title1")
                .content("content1")
                .expose("PUBLIC")
                .status(false)
                .endDate(LocalDate.of(2026,2,1))
                .build();
        Todo todo1 = Todo.builder()
                .title("title2")
                .content("content2")
                .expose("PUBLIC")
                .status(true)
                .endDate(LocalDate.of(2026,2,1))
                .build();
        Todo todo2 = Todo.builder()
                .title("title2")
                .content("content2")
                .expose("PUBLIC")
                .status(false)
                .endDate(LocalDate.of(2022,2,1))
                .build();
        Users users = Users.builder()
                .username("name")
                .email("name@gmail.com")
                .role(List.of(Role.USER.getRole()))
                .build();

        usersRepository.save(users);
        todo.addUsers(users);
        todo1.addUsers(users);
        todo2.addUsers(users);

        todoRepository.save(todo);
        todoRepository.save(todo1);
        todoRepository.save(todo2);


        doReturn(3)
                .when(todoRepository).findByDate(anyLong(), eq(LocalDate.now(fixedClock)));
        doReturn(1)
                .when(todoRepository).findByDateAndStatus(anyLong(), eq(LocalDate.now(fixedClock)));

        double percentage = attendService.getPercentage(1L);
        System.out.println(percentage);
        assertThat(percentage).isEqualTo(33.0);

    }

    @Test
    void saveAttend() {
        Users users = Users.builder()
                .username("name")
                .email("name@gmail.com")
                .role(List.of(Role.USER.getRole()))
                .build();
        TeamSaveDto teamSaveDto = TeamSaveDto.builder()
                .title("title")
                .explanation("explanation")
                .limit(5)
                .build();
        Team team = teamSaveDto.toEntity();

        usersRepository.save(users);
        teamRepository.save(team);

        Attend attend = Attend.builder()
                .date(LOCAL_DATE)
                .percentage(33.0)
                .team(team)
                .userId(users.getUsersId())
                .build();

        doReturn(33.0)
                .when(attendService).getPercentage(anyLong());
        doReturn(attend)
                .when(attendRepository).save(any(Attend.class));

        Team team1 = attendService.saveAttend(users, team);

        assertThat(team1.getAttends().get(0)).isEqualTo(attend);

    }

    @Test
    void saveAll() {

    }
}
