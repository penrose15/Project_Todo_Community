package com.example.to_do_list.repository.todo;

import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.example.to_do_list.dto.todo.TodoTitleResponsesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("select new com.example.to_do_list.dto.todo.TodoResponsesDto(t.id ,t.title, t.status) " +
            " from Todo t " +
            "where :date >= t.date " +
            "and :date <= t.endDate " +
            "and t.status = FALSE " +
            "and t.users.usersId = :usersId")
    Page<TodoResponsesDto> findByDateNow(Pageable pageable, LocalDate date, Long usersId);

    @Query("select count(t) " +
            "from Todo t " +
            "where t.users.usersId = :id " +
            "and :date >= t.date " +
            "and :date <= t.endDate " +
            "and t.expose = 'PUBLIC' " +
            "and (t.finishDate is null or t.finishDate = :date)")
    int findByDate(Long id,LocalDate date);

    @Query("select new com.example.to_do_list.dto.todo.TodoResponsesDto(t.id, t.title, t.status) " +
            "from Todo t " +
            "where t.users.usersId = :id " +
            "and t.status = TRUE " +
            "and t.expose = 'PUBLIC' " +
            "and t.finishDate = :date")
    int findByDateAndStatus(Long id,LocalDate date);

    @Query("select new com.example.to_do_list.dto.todo.TodoResponsesDto(t.id, t.title, t.status) " +
            "from Todo t " +
            "where t.users.id = :usersId AND t.status = True ")
    Page<TodoResponsesDto> findByUsersIdAndStatusIsTrue(Long usersId, PageRequest pageRequest);

    @Query("select new com.example.to_do_list.dto.todo.TodoResponsesDto(t.id, t.title, t.status) " +
            "from Todo t  " +
            "where t.users.id = :usersId and t.status = FALSE ")
    Page<TodoResponsesDto> findByUsersIdAndStatusIsFalse(Long usersId, PageRequest pageRequest);

    @Query("SELECT new com.example.to_do_list.dto.todo.TodoTitleResponsesDto(t.id, t.title, t.status) " +
            " FROM Todo t " +
            " WHERE t.users.id = :usersId AND (:date >= t.date AND :date <= t.endDate) AND t.expose = 'PUBLIC' GROUP BY t.id")
    List<TodoTitleResponsesDto> findByUsersIdAndIsExposeAndDate(@Param("usersId") Long usersId, LocalDate date);

    @Query("select new com.example.to_do_list.dto.todo.TodoResponsesDto(t.id, t.title, t.status)" +
            "from Todo t " +
            "where t.users.id = :usersId and t.status = FALSE and t.category.categoryId = :categoryId")
    List<TodoResponsesDto> findByUsersIdAndStatusAndCategoryId(long usersId, long categoryId);
}
