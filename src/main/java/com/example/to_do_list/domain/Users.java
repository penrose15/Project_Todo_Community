package com.example.to_do_list.domain;

import com.example.to_do_list.baseentity.BaseEntity;
import com.example.to_do_list.domain.role.Role;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class Users extends BaseEntity {
    @PrePersist
    void prePersist() {
        if(nickname == null) {
            nickname = this.username; //닉네임 초반 이름은 OAuth2에서 받은 username으로 설정
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long usersId;

    @Column
    private String username;

    @Column
    private String nickname;

    @Column(unique = true)
    private String email;

    private String profile;

    @OneToMany(mappedBy = "users", cascade = CascadeType.PERSIST)
    private List<Todo> todoList;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    private Team team;

    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public void addTodoList(Todo todo) {
        this.todoList.add(todo);
    }
    public void resignTeam() {
        this.team = null;
    }

    public void joinTeam(Team team) {
        this.team = team;
    }

    public Users update(String username, String picture) {
        this.username = username;
        this.profile = profile;

        return this;
    }

    @Builder
    public Users(String username, String email, String profile, Role role) {
        this.username = username;
        this.email = email;
        this.profile = profile;
        this.role = role;
    }
}
