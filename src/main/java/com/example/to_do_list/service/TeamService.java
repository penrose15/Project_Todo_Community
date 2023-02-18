package com.example.to_do_list.service;

import com.example.to_do_list.common.exception.BusinessLogicException;
import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.team.*;
import com.example.to_do_list.dto.todo.TodoTitleResponsesDto;
import com.example.to_do_list.repository.TeamRepository;
import com.example.to_do_list.repository.UsersRepository;
import com.example.to_do_list.repository.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.to_do_list.common.exception.ExceptionCode.*;

@Log4j2
@Transactional
@RequiredArgsConstructor
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;
    private final TodoRepository todoRepository;

    public Long save(TeamSaveDto teamSaveDto, Long usersId) {
        Optional<Users> users = usersRepository.findById(usersId);
        Users users1 = users.orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));

        Team team = teamSaveDto.toEntity();
        team.setHostUserId(users1.getUsersId());

        team.setUsersList(new ArrayList<>());
        team.addUsers(users1);

        Team team1 = teamRepository.save(team);

        users1.joinTeam(team1);

        usersRepository.save(users1);

        return team1.getTeamId();
    }

    public Long update(TeamUpdateDto teamUpdateDto, Long teamId, Long usersId) {
        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new BusinessLogicException(TEAM_NOT_FOUND));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessLogicException(TEAM_NOT_FOUND));

        verifyingHosts(team.getHostUserId(), users.getUsersId());

        team.updateTeam(teamUpdateDto);

        return team.getTeamId();
    }

    public void deleteUser(Long teamId,Long hostsId, Long usersId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessLogicException(TEAM_NOT_FOUND));
        verifyingHosts(team.getHostUserId(), hostsId);

        List<Users> usersList = team.getUsersList();

        Users users = usersRepository.findById(usersId)
                        .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
        try {
            verifyingUsers(usersList, usersId);
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        usersList.remove(users);
        users.setTeam(null);
        team.setUsersList(usersList);

        teamRepository.save(team);
        usersRepository.save(users);
    }

    public Long mandateHost(Long teamId, Long hostsId, Long usersid) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessLogicException(TEAM_NOT_FOUND));
        verifyingHosts(team.getHostUserId(), hostsId);
        List<Users> usersList = team.getUsersList();
        verifyingUsers(usersList, usersid);
        team.setHostUserId(usersid);

        Users hostUsers = usersRepository.findById(hostsId)
                        .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
        usersList.add(hostUsers);

        team.setUsersList(usersList);

        return usersid;
    }
    public TeamResponseDto showTeamDetails(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessLogicException(TEAM_NOT_FOUND));
        return TeamResponseDto.builder()
                .teamId(teamId)
                .title(team.getTitle())
                .explanation(team.getExplanation())
                .limit(team.getLimits())
                .criteria(team.getCriteria())
                .build();
    }

    public TeamDetailResponseDto showUsersTodoList(Long teamId, LocalDate date) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessLogicException(TEAM_NOT_FOUND));

        List<UsersTodoDto> list = new ArrayList<>();
        List<Users> usersList = team.getUsersList();
        for (Users users : usersList) {
            List<TodoTitleResponsesDto> todoTitleResponsesDto =
                    todoRepository.findByUsersIdAndIsExposeAndDate(users.getUsersId(), date);
            UsersTodoDto usersTodoDto = UsersTodoDto.builder()
                    .usersId(users.getUsersId())
                    .username(users.getUsername())
                    .todoList(todoTitleResponsesDto)
                    .build();
            list.add(usersTodoDto);
        }

        return TeamDetailResponseDto.builder()
                .teamId(team.getTeamId())
                .title(team.getTitle())
                .explanation(team.getExplanation())
                .usersTodoDtos(list)
                .build();

    }


    public Page<TeamResponsesDto> teamList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "teamId");

        return teamRepository.findTeamResponsesDto(pageRequest);
    }

    private void verifyingHosts(Long teamHostsId, Long hostId) {
        if(!Objects.equals(teamHostsId, hostId)) {
            throw new BusinessLogicException(ONLY_TEAM_HOST_AVAILABLE);
        }
    }

    public void verifyingUsers(List<Users> usersList, Long usersId) {
        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));

        if(!usersList.contains(users)) {
            throw new BusinessLogicException(USER_NOT_FOUND);
        }
    }

    public void deleteTeam(Long hostsId, Long teamId) {
        Users users = usersRepository.findById(hostsId)
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
        Team team = findTeam(teamId);
        verifyingHosts(team.getHostUserId(), hostsId);
        if(team.getUsersList().size() >1) {
            throw new BusinessLogicException(NUMBER_OF_USERS_IS_NOT_ZERO);
        }
        verifyingHosts(team.getHostUserId(), team.getUsersList().get(0).getUsersId());

        team.setUsersList(new ArrayList<>());
        users.setTeam(null);
        teamRepository.save(team);

        teamRepository.delete(team);
    }
    private Team findTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessLogicException(TEAM_NOT_FOUND));
    }


}
