package com.example.to_do_list.repository;

import com.example.to_do_list.domain.Columns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnsRepository extends JpaRepository<Columns, Long> {
}
