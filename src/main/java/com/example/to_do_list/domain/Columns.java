package com.example.to_do_list.domain;

import com.example.to_do_list.commons.baseentity.BaseEntity;
import com.example.to_do_list.dto.columns.ColumnsResponseDto;
import com.example.to_do_list.dto.columns.ColumnsUpdateDto;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
public class Columns extends BaseEntity {

    @PrePersist
    private void prePersist() {
        this.done = false;
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
    private boolean done;

    @Column
    private boolean expose;

    @ManyToOne
    @JoinColumn(name = "todoList_id")
    private TodoList todoList;

    @Builder
    public Columns(String title, String content, boolean done, boolean expose) {
        this.title = title;
        this.content = content;
        this.done = done;
        this.expose = expose;
    }

    public void updateColumns(ColumnsUpdateDto updateDto) {
        if(updateDto.getTitle() != null) {
            this.title = updateDto.getTitle();
        }
        if(updateDto.getContent() != null) {
            this.content = updateDto.getContent();
        }
    }

}
