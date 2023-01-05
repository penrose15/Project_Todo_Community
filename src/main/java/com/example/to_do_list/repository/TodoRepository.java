package com.example.to_do_list.repository;

import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.example.to_do_list.dto.todo.TodoTitleResponsesDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("select new com.example.to_do_list.dto.todo.TodoResponsesDto(t.id ,t.title, t.done) " +
            " from Todo t where :date >= t.date and :date <= t.endDate")
    Slice<TodoResponsesDto> findByDateNow(Pageable pageable, LocalDate date);

    @Query("SELECT new com.example.to_do_list.dto.todo.TodoTitleResponsesDto(t.id, t.title " +
            " FROM Todo t JOIN Users u ON t.usersList.id = u.id " +
            " WHERE (:date >= t.date AND :date <= t.endDate) AND t.expose = 'PUBLIC' GROUP BY u.id )")
    List<TodoTitleResponsesDto> findByUsersIdAndIsExposeAndDate(@Param("usersId") Long usersId, LocalDate date);
}
