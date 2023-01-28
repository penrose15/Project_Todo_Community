package com.example.to_do_list.service;

import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.domain.role.Role;
import com.example.to_do_list.dto.todo.TodoResponseDto;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.example.to_do_list.dto.todo.TodoSaveDto;
import com.example.to_do_list.dto.todo.TodoUpdateDto;
import com.example.to_do_list.repository.TodoRepository;
import com.example.to_do_list.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UsersRepository usersRepository;



    @Test
    void Todo_생성() {
        TodoSaveDto todoSaveDto = TodoSaveDto.builder()
                .title("title")
                .content("content")
                .endDate("2022-01-15")
                .expose("PUBLIC")
                .build();
        Long fakeTodoId = 1L;
        Todo todo = todoSaveDto.toEntity();

        Users users = Users.builder()
                .username("name")
                .email("name@gmail.com")
//                .profile("dog.jpg")
                .role(List.of(Role.USER.getRole()))
                .build();
        Long fakeUsersId = 1L;

        ReflectionTestUtils.setField(users, "usersId", fakeUsersId);
        ReflectionTestUtils.setField(todo, "id", fakeTodoId);

        doReturn(users)
                .when(usersRepository).save(any(Users.class));
        doReturn(Optional.ofNullable(users))
                .when(usersRepository).findById(anyLong());
        doReturn(todo)
                .when(todoRepository).save(any(Todo.class));

        Long newTodoId = todoService.save(todoSaveDto, fakeUsersId);

        assertThat(newTodoId).isEqualTo(fakeTodoId);
        assertThat(todo.getTitle()).isEqualTo("title");
        assertThat(todo.getContent()).isEqualTo("content");
    }

    @Test
    void Todo_update(){
        TodoUpdateDto dto = TodoUpdateDto.builder()
                .title("title")
                .content("content")
                .status(false)
                .endDate("2023-02-15")
                .expose("PUBLIC")
                .build();
        Long fakeTodoId = 1L;
        Todo todo = dto.toEntity();

        Users users = Users.builder()
                .username("name")
                .email("name@gmail.com")
//                .profile("dog.jpg")
                .role(List.of(Role.USER.getRole()))
                .build();
        Long fakeUsersId = 1L;

        ReflectionTestUtils.setField(users, "usersId", fakeUsersId);
        ReflectionTestUtils.setField(todo, "id", fakeTodoId);


        doReturn(Optional.ofNullable(users))
                .when(usersRepository).findById(anyLong());

        doReturn(Optional.ofNullable(todo))
                .when(todoRepository).findById(anyLong());
        doReturn(todo)
                .when(todoRepository).save(any(Todo.class));


        Long newTodoId = todoService.update(fakeTodoId, dto, fakeUsersId);

        assertThat(newTodoId).isEqualTo(fakeTodoId);

    }

    @Test
    void Todo_findById() {
        TodoSaveDto dto = TodoSaveDto.builder()
                .title("title")
                .content("content")
                .endDate("2023-01-23")
                .expose("PUBLIC")
                .build();
        Long fakeTodoId = 1L;
        Todo todo = dto.toEntity();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        TodoResponseDto todoResponseDto = TodoResponseDto.builder()
                .title("title")
                .content("content")
                .status(false)
                .expose("PUBLIC")
                .endDate(LocalDate.of(2023,1,20).format(formatter))
                .build();

        doReturn(Optional.ofNullable(todo))
                .when(todoRepository).findById(anyLong());

        TodoResponseDto result = todoService.findById(fakeTodoId);

        assertThat(result.getTitle()).isEqualTo("title");
    }

    @Test
    void todo_findByDate() {
        TodoResponsesDto todoResponsesDto1 = new TodoResponsesDto(1L, "title1",false);
        TodoResponsesDto todoResponsesDto2 = new TodoResponsesDto(2L, "title2",false);
        List<TodoResponsesDto> list = List.of(todoResponsesDto1, todoResponsesDto2);
        Slice<TodoResponsesDto> slice = new SliceImpl<>(list);

        doReturn(slice)
                .when(todoRepository).findByDateNow(any(PageRequest.class), any(LocalDate.class), anyLong());
        Slice<TodoResponsesDto> list1 = todoService.findByDate(0, 10, LocalDate.now(),anyLong());
        List<TodoResponsesDto> responsesDtos =
                list1.getContent();
        assertThat(list.size()).isEqualTo(responsesDtos.size());
    }


}
