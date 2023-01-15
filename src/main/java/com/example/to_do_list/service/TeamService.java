package com.example.to_do_list.service;

import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.team.*;
import com.example.to_do_list.dto.todo.TodoTitleResponsesDto;
import com.example.to_do_list.repository.TeamRepository;
import com.example.to_do_list.repository.TodoRepository;
import com.example.to_do_list.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;
    private final TodoRepository todoRepository;

    public Long save(TeamSaveDto teamSaveDto, Long usersId) {
        Optional<Users> users = usersRepository.findById(usersId);
        Users users1 = users.orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));

        Team team = teamSaveDto.toEntity();
        team.setHostUserId(users1.getUsersId());
        team.addUsers(users1);

        Team team1 = teamRepository.save(team);
        return team1.getTeamId();
    }

    public Long update(TeamUpdateDto teamUpdateDto, Long teamId, Long usersId) {
        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 팀"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 팀"));

        verifyingHosts(team.getHostUserId(), users.getUsersId());

        team.updateTeam(teamUpdateDto);

        return team.getTeamId();
    }

    public void deleteUser(Long teamId,Long hostsId, Long usersId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 팀"));
        verifyingHosts(team.getHostUserId(), hostsId);

        List<Users> usersList = team.getUsersList();

        Users users = usersRepository.findById(usersId)
                        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));
        verifyingUsers(usersList, usersId);

        usersList.remove(users);
        team.setUsersList(usersList);
    }

    public Long mandateHost(Long teamId, Long hostsId, Long usersid) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 팀"));
        verifyingHosts(team.getHostUserId(), hostsId);
        List<Users> usersList = team.getUsersList();
        verifyingUsers(usersList, usersid);
        team.setHostUserId(usersid);

        Users hostUsers = usersRepository.findById(hostsId)
                        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));
        usersList.add(hostUsers);

        team.setUsersList(usersList);

        return usersid;
    }
    public TeamResponseDto showTeamDetails(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 팀"));
        return TeamResponseDto.builder()
                .teamId(teamId)
                .title(team.getTitle())
                .explanation(team.getExplanation())
                .build();
    }

    public TeamDetailResponseDto showUsersTodoList(Long teamId, LocalDate date) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 팀"));

        List<UsersTodoDto> list = new ArrayList<>();
        List<Users> usersList = team.getUsersList();
        for (Users users : usersList) {
            List<TodoTitleResponsesDto> todoTitleResponsesDto =
                    todoRepository.findByUsersIdAndIsExposeAndDate(users.getUsersId(), date);
            UsersTodoDto usersTodoDto = UsersTodoDto.builder()
                    .usersId(users.getUsersId())
                    .username(users.getUsername())
                    .profile(users.getProfile())
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


    public Slice<TeamResponsesDto> teamList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        Slice<TeamResponsesDto> slice = teamRepository.findTeamResponsesDto(pageRequest);

        return slice;
    }

    private void verifyingHosts(Long teamHostsId, Long hostId) {
        if(teamHostsId != hostId) {
            throw new IllegalArgumentException("팀의 방장만이 가능한 권리입니다.");
        }
    }

    private void verifyingUsers(List<Users> usersList, Long usersId) {
        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));

        if(!usersList.contains(users)) {
            throw new NoSuchElementException("존재하지 않는 유저");
        }
    }


}
