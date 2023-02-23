package com.example.to_do_list.controller;

import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.SingleResponseDto;
import com.example.to_do_list.dto.category.CategoriesResponseDto;
import com.example.to_do_list.dto.category.CategorySaveDto;
import com.example.to_do_list.dto.category.CategoryUpdateDto;
import com.example.to_do_list.dto.todo.TodoSaveDto;
import com.example.to_do_list.service.CategoryService;
import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/category")
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    private final UsersService usersService;

    @PostMapping("/")
    public ResponseEntity saveCategory(@RequestBody CategorySaveDto categorySaveDto,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usersId = usersService.findByEmail(userDetails.getUsername());

        categorySaveDto.setUsersId(usersId);
        String category = categoryService.save(categorySaveDto);
        return new ResponseEntity(new SingleResponseDto<>(category), HttpStatus.CREATED);
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity updateCategory(@PathVariable(name = "categoryId") Long categoryId,
                                         @RequestBody CategoryUpdateDto categoryUpdateDto,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usersId = usersService.findByEmail(userDetails.getUsername());
        String category = categoryService.update(categoryUpdateDto, categoryId, usersId);

        return new ResponseEntity(new SingleResponseDto<>(category), HttpStatus.OK);
    }

    @PostMapping("/{categoryId}/todo")
    public ResponseEntity addTodoListToCategory(@PathVariable(name = "categoryId") Long categoryId,
                                                @RequestBody TodoSaveDto todoSaveDto,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        String todo = categoryService.addTodoList(todoSaveDto, categoryId, userDetails.getUsername());

        return new ResponseEntity(new SingleResponseDto<>(todo), HttpStatus.CREATED);
    }

    @GetMapping("/categories")
    public ResponseEntity showAllCategories(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long usersId = usersService.findByEmail(userDetails.getUsername());
        List<CategoriesResponseDto> response = categoryService.showAllCategories(usersId);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable(name = "categoryId") Long categoryId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        categoryService.deleteCategory(categoryId, userDetails.getUsername());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
