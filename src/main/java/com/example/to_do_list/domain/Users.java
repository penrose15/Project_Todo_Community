package com.example.to_do_list.domain;

import com.example.to_do_list.baseentity.BaseEntity;
import com.example.to_do_list.domain.role.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long usersId;

    @Column
    private String username;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String password;

    @OneToMany(mappedBy = "users", cascade = CascadeType.PERSIST)
    private List<Todo> todoList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    private Team team;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role = new ArrayList<>();

    private String refreshToken;

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
        return this;
    }

    @Builder
    public Users(String username,
                 String email,
                 List<String> role,
                 String password) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public Users(long usersId, String email, String password, List<String> role) {
        this.usersId = usersId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Users(String email, String password, List<String> role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsersId(long usersId) {
        this.usersId = usersId;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
