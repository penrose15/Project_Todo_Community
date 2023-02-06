package com.example.to_do_list.service;

import com.example.to_do_list.domain.Category;
import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.category.CategoriesResponseDto;
import com.example.to_do_list.dto.category.CategorySaveDto;
import com.example.to_do_list.dto.category.CategoryUpdateDto;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.example.to_do_list.dto.todo.TodoSaveDto;
import com.example.to_do_list.repository.CategoryRepository;
import com.example.to_do_list.repository.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final TodoRepository todoRepository;

    public String save(CategorySaveDto request) {
        Category category = request.toEntity();

        category.addEmptyList();

        Category savedCategory = categoryRepository.save(category);
        return category.getName();
    }

    public String update(CategoryUpdateDto request, long categoryId, long usersId) {
        Category findCategory = verifyById(categoryId);

        if(usersId != categoryId) {
            throw new IllegalArgumentException("본인의 category만 수정 가능");
        }

        findCategory.update(request);

        Category updatedCategory = categoryRepository.save(findCategory);
        return updatedCategory.getName();
    }

    public String addTodoList(TodoSaveDto request, long categoryId, Users users) {
        Todo todo = request.toEntity();
        Category category = verifyById(categoryId);

        todo.setUsers(users);

        Todo savedTodo = todoRepository.save(todo);

        category.addTodo(savedTodo);
        todo.setCategory(category);

        return todo.getCategory().getName();
    }
    /*
     * category 이름과 todo이름과 아이디만 조회되도록 할 것임
     * 이를 위해서는 todo들 조회시 전체 조회가 아니라 아니라 이름과 아이다만
     * 조회 가능하도록 해야 함
     * QueryDSL 을 사용해야 하거나 쿼리를 두번 날리든가 해야 함
     * */
    //todo : controller에 기능 추가하기
    public List<CategoriesResponseDto> showAllCategories(Long usersId) {
        List<Category> categories = categoryRepository.findAllByUsersId(usersId);
        List<CategoriesResponseDto> responses = new ArrayList<>();

        for(int i = 0; i<categories.size(); i++) {
            Category category = categories.get(i);
            List<TodoResponsesDto> todoList
                    = todoRepository.findByUsersIdAndStatusAndCategoryId(usersId, category.getCategoryId());

            CategoriesResponseDto response = CategoriesResponseDto.builder()
                    .name(category.getName())
                    .todoResponsesDtos(todoList)
                    .build();
            responses.add(response);
        }
        return responses;
    }

    public void deleteCategory(long categoryId, Long usersId) {
        Category category = verifyById(categoryId);
        if(category.getUsersId() != usersId) {
            throw new IllegalArgumentException("유저 아이디 일치하지 않음");
        }
        categoryRepository.delete(category);
    }


    private Category verifyById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리"));
    }
}
