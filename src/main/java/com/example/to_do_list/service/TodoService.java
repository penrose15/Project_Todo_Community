package com.example.to_do_list.service;

import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.todo.*;
import com.example.to_do_list.repository.TodoRepository;
import com.example.to_do_list.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
@Transactional
@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UsersRepository usersRepository;

    public Long save(TodoSaveDto todoSaveDto, Long usersId) {
        Todo todo = todoSaveDto.toEntity();

        Users users = findUsersById(usersId);
        todo.setUsers(users);

        Todo saveTodo = todoRepository.save(todo);
        users.addTodoList(todo);
        usersRepository.save(users);

        return saveTodo.getId();
    }

    public Long update(Long id, TodoUpdateDto todoUpdateDto, Long usersId) {
        Users users = findUsersById(usersId);
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 할일입니다."));

        todo.updateColumns(todoUpdateDto);
        todoRepository.save(todo);
        return id;
    }

    public TodoResponseDto findById(Long id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재 하지 않은 할일"));
        return TodoResponseDto.builder()
                .title(todo.getTitle())
                .content(todo.getContent())
                .status(todo.isStatus())
                .expose(todo.getExpose())
                .endDate(todo.getEndDate().format(formatter))
                .build();
    }
    public Long changeStatus(Long id, Long usersId) {
        Users users = findUsersById(usersId);
        List<Todo> list = users.getTodoList();

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("todo not found"));
        if(!list.contains(todo)) {
            throw new IllegalArgumentException("자신의 todo만 변경 가능합니다.");
        }

        todo.updateStatus();
        todoRepository.save(todo);

        return todo.getId();
    }
    public Page<TodoResponsesDto> findByDate(int page, int size, LocalDate date, Long usersId) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        Page<TodoResponsesDto> todos;
        try {
             todos = todoRepository.findByDateNow(pageRequest,date, usersId);
        } catch (NullPointerException e) {
            todos = new PageImpl<>(new ArrayList<>());
        }


        return todos;
    }

    public TodoMainPageDto mainPageDto(List<TodoResponsesDto> list, LocalDate date) {
        int done = 0;
        for(int i = 0; i<list.size(); i++) {
            if(list.get(i).isStatus()) {
                done++;
            }
        }
        int percentage = (int) (Math.round((double)done / (double) list.size() ) * 100);

        return TodoMainPageDto.builder()
                .todoResponsesDto(list)
                .date(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .percentage(percentage)
                .build();
    }

    public void deleteTodo(Long id, Long usersId) {
        Users users = findUsersById(usersId);
        List<Todo> list = users.getTodoList();
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 todo"));
        if(!list.contains(todo)) {
            throw new IllegalArgumentException("본인의 todo만 삭제 가능");
        }
        todoRepository.deleteById(id);
    }

    public void deleteTodos(List<Long> ids, Long usersId) {
        Users users = findUsersById(usersId);
        List<Long> list = new ArrayList<>();
        List<Todo> todoList = users.getTodoList();
        for (Todo todo : todoList) {
            list.add(todo.getId());
        }
        for (Long id : ids) {
            if(!list.contains(id)) {
                throw new IllegalArgumentException("존재하지 않는 todoList");
            }
        }
        todoRepository.deleteAllById(ids);
    }

    public Users findUsersById(Long usersId) {
        return usersRepository.findById(usersId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));
    }
}
