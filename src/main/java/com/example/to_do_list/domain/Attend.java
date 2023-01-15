package com.example.to_do_list.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long attendId;

    private LocalDate date;
    private Long userId;
    private int percentage;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Attend(LocalDate date, Long userId, int percentage, Team team) {
        this.date = date;
        this.userId = userId;
        this.percentage = percentage;
        this.team = team;
    }
}
