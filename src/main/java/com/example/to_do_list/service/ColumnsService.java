package com.example.to_do_list.service;

import com.example.to_do_list.domain.Columns;
import com.example.to_do_list.dto.columns.ColumnsResponseDto;
import com.example.to_do_list.dto.columns.ColumnsSaveDto;
import com.example.to_do_list.dto.columns.ColumnsUpdateDto;
import com.example.to_do_list.repository.ColumnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ColumnsService {
    private final ColumnsRepository columnsRepository;

    public Long save(ColumnsSaveDto columnsSaveDto) {
        Columns columns = columnsSaveDto.toEntity();
        Columns saveColumns = columnsRepository.save(columns);

        return saveColumns.getId();
    }

    public Long update(Long id,ColumnsUpdateDto columnsUpdateDto) {
        Columns columns = columnsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 할일입니다."));

        columns.updateColumns(columnsUpdateDto);
        return id;
    }

    public ColumnsResponseDto findById(Long id) {
        Columns columns = columnsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재 하지 않은 할일"));
        return ColumnsResponseDto.builder()
                .title(columns.getTitle())
                .content(columns.getContent())
                .done(columns.isDone())
                .expose(columns.isExpose())
                .build();
    }






}
