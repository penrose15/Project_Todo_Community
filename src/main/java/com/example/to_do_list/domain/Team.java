package com.example.to_do_list.domain;

import com.example.to_do_list.baseentity.BaseEntity;
import com.example.to_do_list.dto.team.TeamUpdateDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "teams")
public class Team extends BaseEntity {

//    @PrePersist
//    void prePersist() {
//        if(this.usersList == null) {
//            usersList = new ArrayList<>();
//        }
//    }
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

    @OneToMany(mappedBy = "team")
    private List<Users> usersList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "attend_id")
    private Attend attend;

    @Builder
    public Team(long hostUserId, String title, String explanation, int limits) {
        this.hostUserId = hostUserId;
        this.title = title;
        this.explanation = explanation;
        this.limits = limits;
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
