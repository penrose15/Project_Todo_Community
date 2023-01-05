package com.example.to_do_list.repository;

import com.example.to_do_list.domain.Team;
import com.example.to_do_list.dto.team.TeamResponsesDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select new com.example.to_do_list.dto.team.TeamResponsesDto(t.id, t.title, t.explanation, t.limit) " +
            " from Team t")
    Slice<TeamResponsesDto> findTeamResponsesDto(Pageable pageable);
}
