package com.haidar.coffeemanagementsystem.service;

import com.haidar.coffeemanagementsystem.models.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    ResponseEntity<String> addNewCategory(Map<String, String> requestMap);

    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    ResponseEntity<String> upadateCategory(Map<String, String> requestMap);
}
