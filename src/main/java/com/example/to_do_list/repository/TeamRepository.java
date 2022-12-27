package com.example.to_do_list.repository;

import com.example.to_do_list.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
