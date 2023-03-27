package com.example.to_do_list.domain;

import com.example.to_do_list.baseentity.BaseEntity;
import com.example.to_do_list.domain.role.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @NotBlank
    @Column
    private String username;

    @Email(regexp = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")
    @Column
    private String password;

    @OneToMany(mappedBy = "users")
    private List<Todo> todoList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    private Team team;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role = new ArrayList<>();

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
    public void changeTeam(Team team) {
        this.team = team;
    }

    public void changePassword(String password) {
        this.password = password;
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

}
