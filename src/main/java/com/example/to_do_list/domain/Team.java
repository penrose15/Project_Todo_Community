package com.example.to_do_list.domain;

import com.example.to_do_list.baseentity.BaseEntity;
import com.example.to_do_list.dto.team.TeamUpdateDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "teams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long teamId;

    @Column
    private long hostUserId;

    @Column
    private String title;

    @Column(length = 500)
    private String explanation;

    @Column
    private int limits;

    @Column
    private int criteria;

    @OneToMany(mappedBy = "team")
    private List<Users> usersList = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Attend> attends = new ArrayList<>();

    @Builder
    public Team(long hostUserId, String title, String explanation, int limits, int criteria) {
        this.hostUserId = hostUserId;
        this.title = title;
        this.explanation = explanation;
        this.limits = limits;
        this.criteria = criteria;
    }


    public void setHostUserId(long hostUserId) {
        this.hostUserId = hostUserId;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    public void addUsers(Users users) {
        this.usersList.add(users);
    }

    public void addAttendList() {
        this.attends = new ArrayList<>();
    }

    public void addAttends(Attend attend) {
        if(!this.attends.contains(attend)) {
            this.attends.add(attend);

        }
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
        if(teamUpdateDto.getLimit() != null) {
            this.limits = teamUpdateDto.getLimit();
        }
        if(teamUpdateDto.getCriteria() != null) {
            this.criteria = teamUpdateDto.getCriteria();
        }
    }
}
