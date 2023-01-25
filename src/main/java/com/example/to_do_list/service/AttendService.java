package com.example.to_do_list.service;

import com.example.to_do_list.domain.Attend;
import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.repository.AttendRepository;
import com.example.to_do_list.repository.TeamRepository;
import com.example.to_do_list.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AttendService {

    private final Clock clock;
    private final AttendRepository attendRepository;
    private final TeamRepository teamRepository;
    private final TodoRepository todoRepository;

    public void saveAll() {
        System.out.println("start");
        List<Team> allTeam = teamRepository.findAll();
        if(allTeam == null ||allTeam.isEmpty()) return;

        for (Team team : allTeam) {
            if (team.getUsersList() != null && !team.getUsersList().isEmpty()){
                List<Users> usersList = team.getUsersList();
                for (Users users : usersList) {
                    team = saveAttend(users, team);
                }
            }

        }
        teamRepository.saveAll(allTeam);
        System.out.println("done");

    }

    public Team saveAttend(Users users, Team team) {
        Attend attend = Attend.builder()
                .date(LocalDate.now())
                .percentage(getPercentage(users.getUsersId()))
                .team(team)
                .userId(users.getUsersId())
                .build();
        attend = attendRepository.save(attend);
        if(team.getAttends() == null) {
            team.addAttendList();
        }
        team.addAttends(attend);

        return team;
    }

    public double getPercentage(Long usersId) {
        LocalDate now = LocalDate.now(clock);
        int findAll = todoRepository.findByDate(usersId, now);
        int findAllDone = todoRepository.findByDateAndStatus(usersId, now);

        System.out.println(findAll);
        System.out.println(findAllDone);

        double result = 0;
        if(findAll != 0){
            result = (int)Math.round((double) findAllDone * 100 / (double) findAll);
        }
        if(findAll == 0) {
            result = 0;
        }
        return result;
    }
}
