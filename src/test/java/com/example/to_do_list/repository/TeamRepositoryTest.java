package com.example.to_do_list.repository;

import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.domain.role.Role;
import com.example.to_do_list.dto.team.TeamResponsesDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    void addData() {
        Users users1 = Users.builder()
                .email("123@gmail.com")
                .profile("dog.png")
                .username("users1")
                .role(Role.USER)
                .build();
        Users users2 = Users.builder()
                .email("456@gmail.com")
                .profile("cat.png")
                .username("users2")
                .role(Role.USER)
                .build();

        usersRepository.save(users1);
        usersRepository.save(users2);

        List<Users> usersList = new ArrayList<>();
        usersList.add(users2);

        Team team1 = Team.builder()
                .hostUserId(1L)
                .title("title1")
                .explanation("test1")
                .limits(3)
                .build();


        teamRepository.save(team1);

        users2.joinTeam(team1);
        team1.setUsersList(usersList);

        usersRepository.save(users2);
    }
    @AfterEach
    void clean() {
//        teamRepository.deleteAll();
    }

    @Test
    void Team_조회하기() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        Slice<TeamResponsesDto> slice = teamRepository.findTeamResponsesDto(pageRequest);
        List<TeamResponsesDto> list = slice.getContent();

        assertThat(list.get(0).getTitle()).isEqualTo("title1");
        assertThat(list.get(0).getLimits()).isEqualTo(3);
    }
}
