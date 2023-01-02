package com.example.to_do_list.domain;

import com.example.to_do_list.commons.baseentity.BaseEntity;
import com.example.to_do_list.dto.todo.TodoUpdateDto;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Entity
public class Todo extends BaseEntity {

    @PrePersist
    private void prePersist() {
        this.status = false;
        this.date = LocalDate.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @Length(max = 1000)
    @Column(length = 1000)
    private String content;

    @Column
    private boolean status;

    @Column
    private boolean expose;

    @Column
    private LocalDate date;

    @Column
    private LocalDate endDate;

    @Builder
    public Todo(String title, String content, boolean status, boolean expose, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.expose = expose;
        this.endDate = endDate;
    }




    public void updateColumns(TodoUpdateDto updateDto) {
        if(updateDto.getTitle() != null) {
            this.title = updateDto.getTitle();
        }
        if(updateDto.getContent() != null) {
            this.content = updateDto.getContent();
        }
    }

    public boolean updateStatus() {
        this.status = !this.status;
        return this.status;
    }

}
