package com.haidar.coffeemanagementsystem.rest;

import com.haidar.coffeemanagementsystem.models.Category;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryRest {


    @PostMapping(path = "/add")
    ResponseEntity<String> addNewCategory(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "/get")
    ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue);
    //int get categories we passed a value to our function to prive the user from seing the category if it's empty
    //at least we need one product in one category to allow for the user to see it

    @PostMapping(path = "/update")
    ResponseEntity<String> updateCategory(@RequestBody Map<String,String> requestMap);

}
