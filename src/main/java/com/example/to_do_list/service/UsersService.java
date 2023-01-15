package com.example.to_do_list.service;

import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.domain.role.Role;
import com.example.to_do_list.repository.TeamRepository;
import com.example.to_do_list.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
@Transactional
@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final TeamRepository teamRepository;

    public Long findById(Long userId) {
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));
        return users.getUsersId();
    }

    //임시
    public Long save() {
        Users users = Users.builder()
                .username("users")
                .profile("dog.jpg")
                .role(Role.USER)
                .email("abc@gmail.com")
                .build();
        Users users1 = Users.builder()
                .username("users1")
                .profile("dog1.jpg")
                .role(Role.USER)
                .email("bcd@gmail.com")
                .build();
        users = usersRepository.save(users);
        usersRepository.save(users1);

        return users.getUsersId();
    }

    public Long joinTeam(Long teamId, Long usersId) {
        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 팀"));

        if(users.getTeam() != null) {
            throw new IllegalArgumentException("하나의 팀만 참여 가능합니다.");
        }

        users.joinTeam(team);
        team.addUsers(users);

        teamRepository.save(team);
        usersRepository.save(users);

        return teamId;
    }

    public void resignTeam(Long usersId) {
        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원"));

        Team team = users.getTeam();

        team.deleteUsers(users);
        users.resignTeam();

        usersRepository.save(users);
        teamRepository.save(team);
    }
}
