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

import java.time.LocalDate;
import java.util.List;
@Transactional
@Service
@RequiredArgsConstructor
public class AttendService {
    private final AttendRepository attendRepository;
    private final TeamRepository teamRepository;
    private final TodoRepository todoRepository;

    public void saveAll() {
        System.out.println("start");
        List<Team> allTeam = teamRepository.findAll();
        if(allTeam.isEmpty()) return;

        for(int i = 0; i<allTeam.size(); i++) {
            Team team = allTeam.get(i);
            List<Users> usersList = team.getUsersList();
            if(usersList.isEmpty()) continue;
            for (Users users : usersList) {
                saveAttend(users, team);
            }
        }
        System.out.println("done");

    }

    public void saveAttend(Users users, Team team) {
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


    }

    private int getPercentage(Long usersId) {
        int findAll = todoRepository.findByDate(usersId, LocalDate.now());
        int findAllDone = todoRepository.findByDateAndStatus(usersId, LocalDate.now());

        int result = 0;
        if(findAll != 0){
            result = (int)Math.round((double) findAllDone / (double) findAll) * 100;
        }
        return result;
    }
}
