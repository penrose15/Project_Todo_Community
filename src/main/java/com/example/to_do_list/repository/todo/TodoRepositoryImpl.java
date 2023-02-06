package com.example.to_do_list.repository.todo;

import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.to_do_list.domain.QTodo.todo;
import static com.example.to_do_list.domain.QUsers.users;

@RequiredArgsConstructor
@Repository
public class TodoRepositoryImpl implements TodoRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<TodoResponsesDto> searchTodo(String title, String content, Integer priority, String expose, long usersId, Pageable pageable) {
        List<TodoResponsesDto> todoList = queryFactory
                .select(Projections.constructor(TodoResponsesDto.class, todo.id, todo.title, todo.status))
                .from(todo)
                .join(users)
                .on(todo.users.usersId.eq(users.usersId))
                .where(todo.users.usersId.eq(usersId)
                        .and(isTitleContains(title)
                                .or(isContentsContains(content))
                                .or(isPriorityEqual(priority))
                                .or(todo.expose.eq(expose)))
                        .and(todo.status.isFalse()))
                .groupBy(todo.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPQLQuery<TodoResponsesDto> count = queryFactory
                .select(Projections.constructor(TodoResponsesDto.class, todo.id, todo.title, todo.status))
                .from(todo)
                .join(users)
                .on(todo.users.usersId.eq(users.usersId))
                .where(todo.users.usersId.eq(usersId)
                        .and(isTitleContains(title)
                                .or(isContentsContains(content)))
                        .and(todo.status.isFalse()))
                .groupBy(todo.id);
        return PageableExecutionUtils.getPage(todoList, pageable, count::fetchCount);

    }

    @Override
    public TodoResponsesDto findTodoResponses(LocalDate date, long usersId) {
        return queryFactory
                .select(Projections.constructor(TodoResponsesDto.class, todo.id, todo.title, todo.status))
                .from(todo)
                .join(users)
                .on(todo.users.usersId.eq(users.usersId))
                .where(todo.users.usersId.eq(usersId)
                        .and(todo.date.eq(date)))
                .fetchFirst();
    }

    private BooleanExpression isTitleContains(String title) {
        return title != null ? todo.title.contains(title) : null;
    }

    private BooleanExpression isContentsContains(String contents) {
        return contents != null ? todo.content.contains(contents) : null;
    }

    private BooleanExpression isPriorityEqual(Integer priority) {
        return priority != null ? todo.priority.eq(priority) : null;
    }
}
