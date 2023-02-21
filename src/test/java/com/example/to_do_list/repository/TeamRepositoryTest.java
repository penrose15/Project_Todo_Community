package com.example.to_do_list.repository;

import com.example.to_do_list.common.JPAConfig;
import com.example.to_do_list.common.queryDSL.QueryDslConfig;
import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.domain.role.Role;
import com.example.to_do_list.dto.team.TeamResponsesDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({JPAConfig.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("set1")
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);
    }

    @BeforeEach
    void addData() {
        Users users1 = Users.builder()
                .email("123@gmail.com")
                .username("users1")
                .role(List.of(Role.USER.getRole()))
                .build();
        Users users2 = Users.builder()
                .email("456@gmail.com")
                .username("users2")
                .role(List.of(Role.USER.getRole()))
                .build();

        Team team1 = Team.builder()
                .hostUserId(1L)
                .title("title1")
                .explanation("test1")
                .criteria(2)
                .limits(3)
                .build();


        teamRepository.save(team1);

        List<Users> usersList = new ArrayList<>();
        usersList.add(users2);

        users2.joinTeam(team1);
        team1.setUsersList(usersList);

        usersRepository.save(users1);
        usersRepository.save(users2);






    }
    @AfterEach
    void clean() {
//        teamRepository.deleteAll();
    }

    @Test
    void Team_조회하기() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        Page<TeamResponsesDto> slice = teamRepository.findTeamResponsesDto(pageRequest);
        List<TeamResponsesDto> list = slice.getContent();

        assertThat(list.get(0).getTitle()).isEqualTo("title1");
        assertThat(list.get(0).getLimits()).isEqualTo(3);
    }
}
