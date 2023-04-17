package com.haidar.coffeemanagementsystem.restImpl;

import com.haidar.coffeemanagementsystem.cofeUtils.CafeUtil;
import com.haidar.coffeemanagementsystem.models.Category;
import com.haidar.coffeemanagementsystem.rest.CategoryRest;
import com.haidar.coffeemanagementsystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    CategoryService categoryService;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            return categoryService.addNewCategory(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponsseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            return categoryService.getAllCategory(filterValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            return categoryService.upadateCategory(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponsseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
