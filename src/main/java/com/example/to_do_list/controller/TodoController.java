package com.example.to_do_list.controller;

import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.dto.MultiResponseDto;
import com.example.to_do_list.dto.todo.*;
import com.example.to_do_list.service.TodoService;
import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        Long usersId = usersService.findByEmail(user.getUsername());
        return new ResponseEntity<>(todoService.save(request,usersId), HttpStatus.CREATED);
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id,
                       @RequestBody TodoUpdateDto request,
                       @AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
        Long usersId = usersService.findByEmail(email);
//        Long usersId = 1L;
        return new ResponseEntity<>(todoService.update(id, request, usersId), HttpStatus.OK);
    }

    @GetMapping("/posts/category")
    public ResponseEntity<Long> changeCategory(@RequestParam Long todoId,
                                               @RequestParam Long categoryId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        usersService.findByEmail(userDetails.getUsername());
        Long response = todoService.changeCategories(todoId, categoryId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<TodoResponseDto> findById(@PathVariable Long id,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        usersService.findByEmail(userDetails.getUsername());
        return new ResponseEntity<>(todoService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/posts/done/{id}")
    public ResponseEntity<Long> todoDone(@PathVariable Long id,
                            @AuthenticationPrincipal CustomUserDetails user) {

        Long usersId = usersService.findByEmail(user.getEmail());
//        Long usersId = 1L;
        return new ResponseEntity<>(todoService.changeStatus(id, usersId), HttpStatus.OK);
    }

    @GetMapping("/posts/today")
    public ResponseEntity findToday(@RequestParam int page,
                                     @RequestParam int size,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usersId = usersService.findByEmail(userDetails.getEmail()); //verify

        Page<TodoResponsesDto> request = todoService.findByDate(page-1, size, LocalDate.now(), usersId);
        List<TodoResponsesDto> list = request.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(list, request), HttpStatus.OK);
    }

    @GetMapping("/posts/days")
    public ResponseEntity findNextDay(@RequestParam int page,
                                      @RequestParam int size,
                                      @RequestParam String date,
                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long usersId = usersService.findByEmail(customUserDetails.getEmail()); //verify

        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        Page<TodoResponsesDto> request = todoService.findByDate(page-1, size, localDate, usersId);
        List<TodoResponsesDto> list = request.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(list, request), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity searchTodo(@RequestParam int page,
                                     @RequestParam int size,
                                     @RequestParam(required = false) String title,
                                     @RequestParam(required = false) String content,
                                     @RequestParam(required = false) Integer priority,
                                     @RequestParam(required = false) String expose,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usersId = usersService.findByEmail(userDetails.getEmail());

        Page<TodoResponsesDto> request = todoService.searchByTitleOrContents(page, size, title, content, priority, expose, usersId);
        List<TodoResponsesDto> list = request.getContent();

        return new ResponseEntity(new MultiResponseDto<>(list, request), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long usersId = usersService.findByEmail(user.getEmail());
        todoService.deleteTodo(id, usersId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/posts")
    public ResponseEntity<Void> deleteTodos(@RequestBody TodoIdsDto ids,
                                            @AuthenticationPrincipal CustomUserDetails user) {
        Long usersId = usersService.findByEmail(user.getEmail());
        todoService.deleteTodos(ids.getIds(), usersId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
