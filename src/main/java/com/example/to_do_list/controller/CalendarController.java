package com.example.to_do_list.controller;

import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.dto.calendar.CalendarDto;
import com.example.to_do_list.dto.response.SingleResponseDto;
import com.example.to_do_list.service.CalendarService;
import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;
    private final UsersService usersService;

    @GetMapping("/{year}/{month}") //no constructor found...error --> 해결함
    public ResponseEntity getTodosByMonth(@PathVariable int year,
                                          @PathVariable int month,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usersId = usersService.findByEmail(userDetails.getUsername());
        CalendarDto calendarDto = calendarService.todoMonth(year, month, usersId);

        return new ResponseEntity(new SingleResponseDto<>(calendarDto), HttpStatus.OK);
    }
}
