package com.example.to_do_list.repository;

import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.dto.todo.TodoResponseDto;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void addData() {
        String title = "title";
        String content = "content";
        boolean done = false;
        boolean expose = true;
        LocalDate endDate = LocalDate.now().plusDays(1L);

        Todo todo1 = Todo.builder()
                .title(title)
                .content(content)
                .status(done)
                .expose(expose)
                .endDate(endDate)
                .build();
        Todo todo2 = Todo.builder()
                .title(title + "1")
                .content(content)
                .status(done)
                .expose(expose)
                .endDate(endDate.plusDays(1L))
                .build();
        Todo todo3 = Todo.builder()
                .title(title+"2")
                .content(content)
                .status(done)
                .expose(expose)
                .endDate(endDate.plusDays(2L))
                .build();
        Todo todo4 = Todo.builder()
                .title(title+"3")
                .content(content)
                .status(done)
                .expose(expose)
                .endDate(endDate.plusDays(3L))
                .build();

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);
        todoRepository.save(todo4);
    }

    @AfterEach
    void clean() {
        todoRepository.deleteAll();
    }

    @Test
    void TODOLIST_오늘치_불러오기() {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.Direction.ASC, "id");
        Slice<TodoResponsesDto> slice = todoRepository.findByDateNow(pageRequest,LocalDate.now());
        List<TodoResponsesDto> list = slice.getContent();

        assertThat(list.size()).isEqualTo(4);
        assertThat(list.get(0).getTitle()).contains("title");
        assertThat(list.get(1).getTitle()).contains("title");
        assertThat(list.get(2).getTitle()).contains("title");
    }
    @Test
    void TODO_불러오기() {
        Optional<Todo> todo= todoRepository.findById(2L);
        Todo findTodo = todo.orElseThrow(() -> new NoSuchElementException());

        assertThat(findTodo.getTitle()).isEqualTo("title1");
    }

}
