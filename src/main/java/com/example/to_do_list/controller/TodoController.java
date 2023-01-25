package com.example.to_do_list.controller;

import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.common.security.userdetails.CustomUserDetailsService;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.todo.TodoResponseDto;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.example.to_do_list.dto.todo.TodoSaveDto;
import com.example.to_do_list.dto.todo.TodoUpdateDto;
import com.example.to_do_list.service.TodoService;
import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;
    private final UsersService usersService;

    @PostMapping("/posts")
    public ResponseEntity<Long> save(@RequestBody TodoSaveDto request,
                     @AuthenticationPrincipal CustomUserDetails user) {

        System.out.println(">>> " + user);
        Long usersId = usersService.findByEmail(user.getUsers().getEmail());
        return new ResponseEntity<>(todoService.save(request,usersId), HttpStatus.CREATED);
    }

    @PatchMapping("/posts/{id}")
    public Long update(@PathVariable Long id,
                       @RequestBody TodoUpdateDto request,
                       @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long usersId = user.getUsers().getUsersId();
//        Long usersId = 1L;
        return todoService.update(id, request, usersId);
    }

    @GetMapping("/posts/{id}")
    public TodoResponseDto findById(@PathVariable Long id) {
        return todoService.findById(id);
    }
    @GetMapping("/posts/done/{id}")
    public boolean todoDone(@PathVariable Long id,
                            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long usersId = user.getUsers().getUsersId();
//        Long usersId = 1L;
        return todoService.changeStatus(id, usersId);
    }

    @GetMapping("/posts")
    public ResponseEntity findToday(@RequestParam int page,
                                     @RequestParam int size,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usersId = usersService.findByEmail(userDetails.getUsers().getEmail()); //verify

        Slice<TodoResponsesDto> request = todoService.findByDate(page, size, LocalDate.now(), usersId);
        List<TodoResponsesDto> list = request.getContent();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/posts/days")
    public ResponseEntity findNextDay(@RequestParam int page,
                                      @RequestParam int size,
                                      @RequestParam String date,
                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long usersId = usersService.findByEmail(customUserDetails.getUsers().getEmail()); //verify

        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        Slice<TodoResponsesDto> request = todoService.findByDate(page, size, localDate, usersId);
        List<TodoResponsesDto> list = request.getContent();
        System.out.println(list.size());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long usersId = usersService.findById(user.getUsers().getUsersId());
        todoService.deleteTodo(id, usersId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/posts")
    public ResponseEntity<Void> deleteTodos(@RequestBody List<Long> ids,
                                            @AuthenticationPrincipal CustomUserDetails user) {
        Long usersId = usersService.findById(user.getUsers().getUsersId());
        todoService.deleteTodos(ids, usersId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
