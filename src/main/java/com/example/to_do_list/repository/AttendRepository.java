package com.example.to_do_list.repository;

import com.example.to_do_list.domain.Attend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AttendRepository extends JpaRepository<Attend, Long> {

    @Query("select a" +
            " from Attend a " +
            " where a.team.teamId = :teamId and " +
            " a.date > :date and a.date <= current_date ")
    List<Attend> findByTeamIdAndDate(Long teamId, LocalDate date);
}
