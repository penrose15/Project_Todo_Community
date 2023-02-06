package com.example.to_do_list.service;

import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.CalendarDto;
import com.example.to_do_list.dto.todo.TodoCalendarDTO;
import com.example.to_do_list.repository.UsersRepository;
import com.example.to_do_list.repository.todo.TodoRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final TodoRepositoryImpl todoRepositoryImpl;

    public CalendarDto todoMonth(int year, int month, Long usersId) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, startDate.getDayOfMonth());


        List<TodoCalendarDTO> todoCalendarDTOS = todoRepositoryImpl.findTodoByMonth(startDate, endDate, usersId);
        return CalendarDto.builder()
                .year(year)
                .month(month)
                .todoCalendarDTOS(todoCalendarDTOS)
                .build();
    }
}
