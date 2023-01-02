package com.example.to_do_list.repository;

import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("select new com.example.to_do_list.dto.todo.TodoResponsesDto(t.title, t.done) " +
            " from Todo t where :date >= t.date and :date <= t.endDate")
    Slice<TodoResponsesDto> findByDateNow(LocalDate date);
}
