package com.example.to_do_list.domain;

import com.example.to_do_list.commons.baseentity.BaseEntity;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long hostUserId;

    @Column(length = 500)
    private String explanation;

    @OneToMany(mappedBy = "team")
    private List<Attendance> attendance = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Long> usersIdList = new ArrayList<>();

}
