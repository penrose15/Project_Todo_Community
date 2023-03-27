package com.example.to_do_list.domain;

import com.example.to_do_list.baseentity.BaseEntity;
import com.example.to_do_list.dto.todo.TodoUpdateDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {

    @PrePersist
    private void prePersist() {
        this.status = false;
        this.date = LocalDate.now();
        if(this.endDate == null) {
            this.endDate = LocalDate.now();
        }
        if(this.priority == 0) {
            this.priority = 4;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Length(max = 1000)
    @Column
    private String content;

    @Column
    private boolean status;

    @Column
    private String expose;

    @Range(min = 1, max = 4)
    private int priority;

    @Column
    private LocalDate date;

    @Column
    private LocalDate endDate;

    @Column
    private LocalDate finishDate;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Todo(String title, String content, boolean status, int priority,String expose, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.priority = priority;
        this.expose = expose;
        this.endDate = endDate;
    }

    public void addUsers(Users users) {
        this.users = users;
    }

    public void updateColumns(TodoUpdateDto updateDto) {
        if(updateDto.getTitle() != null) {
            this.title = updateDto.getTitle();
        }
        if(updateDto.getContent() != null) {
            this.content = updateDto.getContent();
        }
        if(updateDto.getExpose() != null) {
            this.expose = updateDto.getExpose();
        }
        if(updateDto.getPriority() != 0) {
            this.priority = updateDto.getPriority();
        }
        if(updateDto.getEndDate() != null) {
            this.endDate = LocalDate.parse(updateDto.getEndDate());
        }
    }

    public Todo updateStatus(Todo todo) {
        todo.status = !todo.status;

        if(todo.status) { //만약 status가 true면 끝낸 날짜 오늘로 설정
            todo.finishDate = LocalDate.now();
        } else { //만약 status가 false라면 끝낸 날짜 삭제
            todo.finishDate = null;
        }
        return todo;
    }

    public void addCategory(Category category) {
        if(this.category != category) {
            this.category = category;
        }
    }
}
