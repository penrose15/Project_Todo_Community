package com.example.to_do_list.service;

import com.example.to_do_list.common.security.utils.CustomAuthorityUtils;
import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.domain.role.Role;
import com.example.to_do_list.dto.user.UsersSaveDto;
import com.example.to_do_list.repository.TeamRepository;
import com.example.to_do_list.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    public Long findById(Long userId) {
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));
        return users.getUsersId();
    }

    public Long findByEmail(String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("not found"));
        return users.getUsersId();
    }

    public String createUsers(UsersSaveDto users) {
        verifyEmail(users.getEmail());

        String encryptedPassword = passwordEncoder.encode(users.getPassword());
        users.setPassword(encryptedPassword);

        List<String> roleList = authorityUtils.createRoles(users.getEmail());
        users.setRoles(roleList);

        Users users1 = users.toEntity();
        usersRepository.save(users1);

        return users1.getEmail();
    }



    //임시
    public Long save() {
        Users users = Users.builder()
                .username("users")
                .role(List.of(Role.USER.getRole()))
                .email("abc@gmail.com")
                .build();
        Users users1 = Users.builder()
                .username("users1")
                .role(List.of(Role.USER.getRole()))
                .email("bcd@gmail.com")
                .build();
        users = usersRepository.save(users);
        usersRepository.save(users1);

        return users.getUsersId();
    }

    public Long joinTeam(Long teamId, String email) {
        Users users = usersRepository.findByEmail(email)
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

    public void resignTeam(String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원"));

        Team team = users.getTeam();

        team.deleteUsers(users);
        users.resignTeam();

        usersRepository.save(users);
        teamRepository.save(team);
    }

    public Users getUser(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 이메일"));
    }

    private void verifyEmail(String email) {
        Optional<Users> users = usersRepository.findByEmail(email);
        if(users.isPresent()) throw new IllegalArgumentException("이메일이 존재합니다.");
    }
}
