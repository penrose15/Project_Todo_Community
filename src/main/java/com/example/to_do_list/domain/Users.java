package com.example.to_do_list.domain;

import com.example.to_do_list.commons.baseentity.BaseEntity;
import com.example.to_do_list.domain.role.Role;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;

    @Column(unique = true)
    private String email;

    private String profile;

    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToMany(mappedBy = "users")
    private List<TodoList> todoLists = new ArrayList<>();

    @Builder
    public Users(String username, String email, String profile, Role role) {
        this.username = username;
        this.email = email;
        this.profile = profile;
        this.role = role;
    }
}
