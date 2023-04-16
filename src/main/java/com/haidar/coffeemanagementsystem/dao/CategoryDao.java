package com.haidar.coffeemanagementsystem.dao;

import com.haidar.coffeemanagementsystem.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category, Integer> {
    @Query("select c from Category c")
    List<Category> getAllCategory();
}
