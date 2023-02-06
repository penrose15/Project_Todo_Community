package com.example.to_do_list.repository;

import com.example.to_do_list.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.usersId = :usersId")
    List<Category> findAllByUsersId(Long usersId);
}
