package com.haidar.coffeemanagementsystem.restImpl;

import com.haidar.coffeemanagementsystem.cofeUtils.CafeUtil;
import com.haidar.coffeemanagementsystem.rest.CategoryRest;
import com.haidar.coffeemanagementsystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    CategoryService categoryService;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            categoryService.addNewCategory(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponsseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
