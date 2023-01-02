package com.example.to_do_list.service;

import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.dto.todo.TodoResponseDto;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.example.to_do_list.dto.todo.TodoSaveDto;
import com.example.to_do_list.dto.todo.TodoUpdateDto;
import com.example.to_do_list.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public Long save(TodoSaveDto todoSaveDto) {
        Todo todo = todoSaveDto.toEntity();
        Todo saveTodo = todoRepository.save(todo);

        return saveTodo.getId();
    }

    public Long update(Long id, TodoUpdateDto todoUpdateDto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 할일입니다."));

        todo.updateColumns(todoUpdateDto);
        return id;
    }

    public TodoResponseDto findById(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재 하지 않은 할일"));
        return TodoResponseDto.builder()
                .title(todo.getTitle())
                .content(todo.getContent())
                .status(todo.isStatus())
                .expose(todo.isExpose())
                .endDate(todo.getEndDate())
                .build();
    }
    public boolean changeStatus(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("todo not found"));
        return todo.updateStatus();
    }
    public Slice<TodoResponsesDto> findByDate(int page, int size, LocalDate date) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        Slice<TodoResponsesDto> todos = todoRepository.findByDateNow(pageRequest,date);
        return todos;
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    public void deleteTodos(List<Long> ids) {
        todoRepository.deleteAllById(ids);
    }

}
