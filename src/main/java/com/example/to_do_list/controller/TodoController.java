package com.example.to_do_list.controller;

import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.dto.todo.TodoResponseDto;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.example.to_do_list.dto.todo.TodoSaveDto;
import com.example.to_do_list.dto.todo.TodoUpdateDto;
import com.example.to_do_list.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo/v1")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/posts")
    public Long save(@RequestBody TodoSaveDto request) {
        return todoService.save(request);
    }

    @PatchMapping("/posts/{id}")
    public Long update(@PathVariable Long id,
                       @RequestBody TodoUpdateDto request) {
        return todoService.update(id, request);
    }

    @GetMapping("/posts/{id}")
    public TodoResponseDto findById(@PathVariable Long id) {
        return todoService.findById(id);
    }
    @GetMapping("/posts/done/{id}")
    public boolean todoDone(@PathVariable Long id) {
        return todoService.changeStatus(id);
    }

    @GetMapping("/posts")
    public ResponseEntity findToday(@RequestParam int page,
                                     @RequestParam int size) {
        Slice<TodoResponsesDto> request = todoService.findByDate(page, size, LocalDate.now());
        List<TodoResponsesDto> list = request.getContent();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/posts/days")
    public ResponseEntity findNextDay(@RequestParam int page,
                                      @RequestParam int size,
                                      @RequestBody String date) {
        LocalDate localDate = LocalDate.parse(date);
        Slice<TodoResponsesDto> request = todoService.findByDate(page, size, localDate);
        List<TodoResponsesDto> list = request.getContent();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/posts")
    public ResponseEntity<Void> deleteTodos(@RequestBody List<Long> ids) {
        todoService.deleteTodos(ids);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
