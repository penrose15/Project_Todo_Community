package com.example.to_do_list.service;

import com.example.to_do_list.common.exception.BusinessLogicException;
import com.example.to_do_list.common.exception.ExceptionCode;
import com.example.to_do_list.domain.Category;
import com.example.to_do_list.domain.Todo;
import com.example.to_do_list.domain.Users;
import com.example.to_do_list.dto.category.CategoriesResponseDto;
import com.example.to_do_list.dto.category.CategorySaveDto;
import com.example.to_do_list.dto.category.CategoryUpdateDto;
import com.example.to_do_list.dto.todo.TodoResponsesDto;
import com.example.to_do_list.dto.todo.TodoSaveDto;
import com.example.to_do_list.repository.CategoryRepository;
import com.example.to_do_list.repository.UsersRepository;
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

    private final UsersRepository usersRepository;

    public String save(CategorySaveDto request) {
        Category category = request.toEntity();

        category.addEmptyList();

        Category savedCategory = categoryRepository.save(category);
        return category.getName();
    }

    public String update(CategoryUpdateDto request, long categoryId, long usersId) {
        Category findCategory = verifyById(categoryId);

        if(usersId != findCategory.getUsersId()) {
            throw new BusinessLogicException(ExceptionCode.USER_ID_NOT_MATCH);
        }

        findCategory.update(request);

        Category updatedCategory = categoryRepository.save(findCategory);
        return updatedCategory.getName();
    }

    public String addTodoList(TodoSaveDto request, long categoryId, String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        Todo todo = request.toEntity();
        Category category = verifyById(categoryId);

        todo.setUsers(users);


        todo.setCategory(category);

        Todo savedTodo = todoRepository.save(todo);
        category.addTodo(savedTodo);
        categoryRepository.save(category);

        return todo.getTitle();
    }

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

    public void deleteCategory(long categoryId, String email) {
        Category category = verifyById(categoryId);
        Users users = usersRepository.findById(category.getUsersId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        if(!email.equals(users.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.USER_ID_NOT_MATCH);
        }
        categoryRepository.delete(category);
    }


    private Category verifyById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
    }
}
