package com.example.to_do_list.service;

import com.example.to_do_list.common.exception.BusinessLogicException;
import com.example.to_do_list.common.exception.ExceptionCode;
import com.example.to_do_list.domain.Category;
import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.todo.*;
import com.example.to_do_list.repository.CategoryRepository;
import com.example.to_do_list.repository.todo.TodoRepository;
import com.example.to_do_list.repository.UsersRepository;
import com.example.to_do_list.repository.todo.TodoRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.to_do_list.common.exception.ExceptionCode.*;

@Transactional
@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UsersRepository usersRepository;
    private final TodoRepositoryImpl todoRepositoryImpl;
    private final CategoryRepository categoryRepository;

    public Long save(TodoSaveDto todoSaveDto, String email) {
        Todo todo = todoSaveDto.toEntity();

        Users users = findUsersByEmail(email);
        todo.setUsers(users);

        Todo saveTodo = todoRepository.save(todo);
        users.addTodoList(todo);
        usersRepository.save(users);

        return saveTodo.getId();
    }

    public Long update(Long id, TodoUpdateDto todoUpdateDto, String email) {
        Users users = findUsersByEmail(email);
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(TODO_NOT_FOUND));

        todo.updateColumns(todoUpdateDto);
        todoRepository.save(todo);
        return id;
    }

    public Long changeCategories(Long todoId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessLogicException(CATEGORY_NOT_FOUND));
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new BusinessLogicException(TODO_NOT_FOUND));

        todo.setCategory(category);
        category.addTodo(todo);

        todoRepository.save(todo);
        categoryRepository.save(category);

        return todo.getId();
    }

    public TodoResponseDto findById(Long id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new BusinessLogicException(TODO_NOT_FOUND));
        return TodoResponseDto.builder()
                .title(todo.getTitle())
                .content(todo.getContent())
                .status(todo.isStatus())
                .expose(todo.getExpose())
                .endDate(todo.getEndDate().format(formatter))
                .build();
    }
    public Long changeStatus(Long id, String email) {
        Users users = findUsersByEmail(email);
        List<Todo> list = users.getTodoList();

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(TODO_NOT_FOUND));
        if(!list.contains(todo)) {
            throw new BusinessLogicException(INVALID_TODO_UPDATE);
        }

        todo = todo.updateStatus(todo); // status -> true
        todoRepository.save(todo);

        return todo.getId();
    }
    //todo : 정렬 기능 만들기
    public Page<TodoResponsesDto> findByStatusIsDone(int page, int size, String email) {
        Users users = usersRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        return todoRepository.findByUsersIdAndStatusIsTrue(users.getUsersId(), pageRequest);
    }

    public Page<TodoResponsesDto> findStatusIsFalse(int page, int size, Long usersId) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        return todoRepository.findByUsersIdAndStatusIsFalse(usersId, pageRequest);
    }
    public Page<TodoResponsesDto> searchByTitleOrContents(int page, int size, String title, String content, Integer priority, String expose, long usersId) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        return todoRepositoryImpl.searchTodo(title, content, priority, expose,usersId,pageRequest);
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

    public void deleteTodo(Long id, String email) {
        Users users = findUsersByEmail(email);
        List<Todo> list = users.getTodoList();
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(TODO_NOT_FOUND));
        if(!list.contains(todo)) {
            throw new BusinessLogicException(INVALID_TODO_DELETE);
        }
        todoRepository.delete(todo);
    }

    public void deleteTodos(List<Long> ids, String email) {
        Users users = findUsersByEmail(email);
        List<Long> list = new ArrayList<>();
        List<Todo> todoList = users.getTodoList();
        for (Todo todo : todoList) {
            list.add(todo.getId());
        }
        for (Long id : ids) {
            if(!list.contains(id)) {
                throw new BusinessLogicException(TODO_NOT_FOUND);
            }
        }
        for (Long id : ids) {
            todoRepository.deleteById(id);
        }
    }

    public Users findUsersById(Long usersId) {
        return usersRepository.findById(usersId)
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
    }

    public Users findUsersByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
    }
}
