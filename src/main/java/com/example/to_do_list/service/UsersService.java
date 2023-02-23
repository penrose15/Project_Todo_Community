package com.example.to_do_list.service;

import com.example.to_do_list.common.exception.BusinessLogicException;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.example.to_do_list.common.exception.ExceptionCode.*;

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
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
        return users.getUsersId();
    }

    public Long findByEmail(String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
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
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessLogicException(TEAM_NOT_FOUND));

        if(users.getTeam() != null) {
            throw new BusinessLogicException(INVALID_TEAM_JOIN);
        }
        if(team.getLimits() <= team.getUsersList().size()) {
            throw new BusinessLogicException(TEAM_IS_FULL);
        }

        users.joinTeam(team);
        team.addUsers(users);

        teamRepository.save(team);
        usersRepository.save(users);

        return teamId;
    }

    public void resignTeam(String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));

        Team team = users.getTeam();

        team.deleteUsers(users);
        users.resignTeam();

        usersRepository.save(users);
        teamRepository.save(team);
    }

    public Users getUser(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(INVALID_EMAIL));
    }

    public void changePassword(String email,String tmpPassword) {
        Users users = getUser(email);
        users.setPassword(passwordEncoder.encode(tmpPassword));

        usersRepository.save(users);
    }



    private void verifyEmail(String email) {
        Optional<Users> users = usersRepository.findByEmail(email);
        if(users.isPresent()) throw new BusinessLogicException(EMAIL_ALREADY_EXIST);
    }

}
