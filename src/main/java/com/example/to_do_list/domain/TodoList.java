package com.example.to_do_list.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class TodoList {

    @PrePersist
    private void prePersist() {
        this.dates = LocalDate.now();
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private double percentage;

    @Column(updatable = false)
    private LocalDate dates;

    @OneToMany(mappedBy = "lists")
    private List<Columns> columns = new ArrayList<>();

    @Builder
    public TodoList(double percentage, LocalDate dates, List<Columns> columns) {
        this.percentage = percentage;
        this.dates = dates;
        this.columns = columns;
    }
}
