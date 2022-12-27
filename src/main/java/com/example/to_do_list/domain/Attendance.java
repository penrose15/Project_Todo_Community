package com.example.to_do_list.domain;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(unique = true, nullable = false)
    private long usersId;

    @Column
    private LocalDate attendDate;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
