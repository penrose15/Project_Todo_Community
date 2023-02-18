package com.example.to_do_list.service;

import com.example.to_do_list.domain.Attend;
import com.example.to_do_list.domain.Team;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.repository.AttendRepository;
import com.example.to_do_list.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class TeamManagementService {
    private final Clock clock;
    private final TeamRepository teamRepository;
    private final TeamService teamService;
    private final AttendRepository attendRepository;

    public void deleteUser() {

        log.info("delete user");
        List<Team> teamList = teamRepository.findAll();
        for (Team team : teamList) {
            Long hostsId = team.getHostUserId();
            int criteria = team.getCriteria();
            if(criteria > 0) {
                List<Attend> attendList =
                        attendRepository.findByTeamIdAndDate(team.getTeamId(), LocalDate.now(clock).minusDays(criteria));
                Map<Long, List<Attend>> attendMap = attendsMap(attendList);
                List<Long> usersIdList = new ArrayList<>(attendMap.keySet());

                List<Long> userList = getUserList(usersIdList, attendMap, criteria);
                for (Long aLong : userList) {
                    teamService.deleteUser(team.getTeamId(), hostsId, aLong);
                }

            }

        }
    }

    private Map<Long, List<Attend>> attendsMap(List<Attend> attendList) {
        Map<Long, List<Attend>> attendMap = new HashMap<>();
        for (Attend attend : attendList) {
            Long usersId = attend.getUserId();
            if(attendMap.containsKey(usersId)) {
                List<Attend> attends =  attendMap.get(usersId);
                if(attends.contains(attend)) { //중복 방지
                    attends.add(attend);
                    attendMap.put(usersId, attends);
                }
            } else {
                List<Attend> list = new ArrayList<>();
                list.add(attend);
                attendMap.put(usersId, list);
            }
        }
        return attendMap;
    }

    public List<Long> getUserList(List<Long> usersIdList, Map<Long, List<Attend>> attendMap, int criteria) {
        List<Long> userList = new ArrayList<>();
        for (Long aLong : usersIdList) {
            List<Attend> attends = attendMap.get(aLong);
            int count = 0;
            for (Attend attend : attends) {
                if(attend.getPercentage() == 0) {
                    count++;
                }
            }
            if (count >= criteria) {
                userList.add(aLong);
            }
        }
        return userList;
    }
}
