package com.example.to_do_list.repository;

import com.example.to_do_list.domain.Team;
import com.example.to_do_list.dto.team.TeamResponsesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select new com.example.to_do_list.dto.team.TeamResponsesDto(t.teamId, t.title, t.explanation, t.limits, t.criteria) " +
            " from Team t")
    Page<TeamResponsesDto> findTeamResponsesDto(Pageable pageable);
}
