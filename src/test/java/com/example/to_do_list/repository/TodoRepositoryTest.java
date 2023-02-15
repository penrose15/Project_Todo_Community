package com.example.to_do_list.repository;

import com.example.to_do_list.common.JPAConfig;
import com.example.to_do_list.common.queryDSL.QueryDslConfig;
import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.domain.role.Role;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.example.to_do_list.repository.todo.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({JPAConfig.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    void addData() {
        Users users1 = Users.builder()
                .email("123@gmail.com")
//                .profile("dog.png")
                .username("users1")
                .role(List.of(Role.USER.getRole()))
                .build();
        Users users2 = Users.builder()
                .email("456@gmail.com")
//                .profile("cat.png")
                .username("users2")
                .role(List.of(Role.USER.getRole()))
                .build();


        usersRepository.save(users1);
        usersRepository.save(users2);



        String title = "title";
        String content = "content";
        boolean done = false;
        String expose = "PUBLIC";
        LocalDate endDate = LocalDate.now().plusDays(1L);

        Todo todo1 = Todo.builder()
                .title(title)
                .content(content)
                .status(done)
                .expose(expose)
                .endDate(endDate)
                .priority(1)
                .build();
        Todo todo2 = Todo.builder()
                .title(title + "1")
                .content(content)
                .status(done)
                .expose(expose)
                .priority(1)
                .endDate(endDate.plusDays(1L))
                .build();
        Todo todo3 = Todo.builder()
                .title(title+"2")
                .content(content)
                .status(done)
                .expose(expose)
                .priority(1)
                .endDate(endDate.plusDays(2L))
                .build();
        Todo todo4 = Todo.builder()
                .title(title+"3")
                .content(content)
                .status(done)
                .expose(expose)
                .priority(1)
                .endDate(endDate.plusDays(3L))
                .build();

        todo1.setUsers(users1);
        todo2.setUsers(users1);
        todo3.setUsers(users2);
        todo4.setUsers(users2);

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
        List<Users> users = usersRepository.findAll();

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.Direction.ASC, "id");
        Page<TodoResponsesDto> slice = todoRepository.findByDateNow(pageRequest,LocalDate.now(), users.get(0).getUsersId());
        List<TodoResponsesDto> list = slice.getContent();


        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0).getTitle()).contains("title");
        assertThat(list.get(1).getTitle()).contains("title");
    }
    @Test
    void TodoList_다른날짜_불러오기() {
        List<Users> users = usersRepository.findAll();

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.Direction.ASC, "id");
        Page<TodoResponsesDto> slice = todoRepository.findByDateNow(pageRequest,LocalDate.now().plusDays(1),users.get(0).getUsersId());
        List<TodoResponsesDto> list = slice.getContent();
        System.out.println(list);

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0).getTitle()).contains("title");
        assertThat(list.get(1).getTitle()).contains("title");
    }
    @Test
    void USER의_TODO_불러오기() {

    }
    @Test
    void TODO_불러오기() {
        Optional<Todo> todo= todoRepository.findById(2L);
        Todo findTodo = todo.orElseThrow(() -> new NoSuchElementException());

        assertThat(findTodo.getTitle()).isEqualTo("title1");
    }

}
