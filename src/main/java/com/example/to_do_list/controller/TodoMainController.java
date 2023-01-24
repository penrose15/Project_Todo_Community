package com.example.to_do_list.controller;

import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.dto.todo.TodoMainPageDto;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.example.to_do_list.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TodoMainController {
    private final TodoService todoService;

    @GetMapping("/api/todo/main")
    public String main(@RequestParam int page,
                       @RequestParam int size,
                       Model model,
                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long usersId = customUserDetails.getUsers().getUsersId();
        Slice<TodoResponsesDto> request = todoService.findByDate(page, size, LocalDate.now(), usersId);
        List<TodoResponsesDto> list = request.getContent();
        TodoMainPageDto todoMainPageDto = todoService.mainPageDto(list, LocalDate.now());
        model.addAttribute("todolist", todoMainPageDto.getTodoResponsesDto());
        model.addAttribute("date",todoMainPageDto.getDate());
        model.addAttribute("percentage",todoMainPageDto.getPercentage());
        return "todoMain";
    }
}
