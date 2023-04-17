package com.haidar.coffeemanagementsystem.dao;

import com.haidar.coffeemanagementsystem.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryDao extends JpaRepository<Category, Integer> {
    //this query (getAllCategory) return the categories that they ahve product only
    @Query("select c from Category c where c.id in (select p.category from Product p where p.status='true')")
    List<Category> getAllCategory();

    Optional<Category> findByName(String categoryName);
}
