package com.example.to_do_list.domain;

import com.example.to_do_list.commons.baseentity.BaseEntity;
import com.example.to_do_list.dto.team.TeamUpdateDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Team extends BaseEntity {

    @PrePersist
    void prePersist() {
        if(this.attendance == null) {
            attendance = new ArrayList<>();
        }
        if(this.usersList == null) {
            usersList = new ArrayList<>();
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long hostUserId;

    @Column
    private String title;

    @Column(length = 500)
    private String explanation;

    @Column
    private int limit;

    @OneToMany(mappedBy = "team")
    private List<Attendance> attendance;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Long> usersIdList;

    @OneToMany(mappedBy = "team")
    private List<Users> usersList;

    @Builder
    public Team(long hostUserId, String title, String explanation, int limit, List<Attendance> attendance, List<Long> usersIdList, List<Users> usersList) {
        this.hostUserId = hostUserId;
        this.title = title;
        this.explanation = explanation;
        this.attendance = attendance;
        this.limit = limit;
        this.usersIdList = usersIdList;
        this.usersList = usersList;
    }

    public void setHostUserId(long hostUserId) {
        this.hostUserId = hostUserId;
    }

    public void setAttendance(List<Attendance> attendance) {
        this.attendance = attendance;
    }

    public void setUsersIdList(List<Long> usersIdList) {
        this.usersIdList = usersIdList;
    }
    public void addUsersId(Long usersId) {
        this.usersIdList.add(usersId);
    }
    public void addUsers(Users users) {
        this.usersList.add(users);
    }

    public void deleteUsers(Users users) {
        this.usersList.remove(users);
    }

    public void updateTeam(TeamUpdateDto teamUpdateDto) {
        if(teamUpdateDto.getTitle() != null) {
            this.title = teamUpdateDto.getTitle();
        }
        if(teamUpdateDto.getExplanation() != null) {
            this.explanation = teamUpdateDto.getExplanation();
        }
    }
}
