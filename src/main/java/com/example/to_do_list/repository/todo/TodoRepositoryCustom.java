package com.example.to_do_list.repository.todo;

import com.example.to_do_list.dto.todo.TodoCalendarDTO;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepositoryCustom {

    Page<TodoResponsesDto> searchTodo(String title, String content, Integer priority, String expose, long usersId, Pageable pageable);

    TodoResponsesDto findTodoResponses(LocalDate date, long usersId);

    List<TodoCalendarDTO> findTodoByMonth(LocalDate startDate, LocalDate endDate, long usersId);
}
