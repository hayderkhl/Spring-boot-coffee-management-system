package com.haidar.coffeemanagementsystem.serviceImpl;

import com.google.common.base.Strings;
import com.haidar.coffeemanagementsystem.cofeUtils.CafeUtil;
import com.haidar.coffeemanagementsystem.constatnts.CafeConstants;
import com.haidar.coffeemanagementsystem.dao.CategoryDao;
import com.haidar.coffeemanagementsystem.jwt.JwtFilter;
import com.haidar.coffeemanagementsystem.models.Category;
import com.haidar.coffeemanagementsystem.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return CafeUtil.getResponsseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            // Validate category data
            if (!validateCtageory(requestMap, false)) {
                return CafeUtil.getResponsseEntity("Invalid category data", HttpStatus.BAD_REQUEST);
            }
            String categoryName = requestMap.get("name");
            Optional<Category> categoryOptional = categoryDao.findByName(categoryName);
            if (!categoryOptional.isEmpty()) {
                return CafeUtil.getResponsseEntity("Category already exists", HttpStatus.CONFLICT);
            } else {
                Category newCategory = getCategoryFromMap(requestMap, false);
                categoryDao.save(newCategory);

                return CafeUtil.getResponsseEntity("Category added successfully", HttpStatus.OK);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
            return CafeUtil.getResponsseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
       try {
           if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                log.info("inside if ");
                return new ResponseEntity<>(categoryDao.getAllCategory(), HttpStatus.OK);
           }
           return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);

       } catch (Exception e) {
           e.printStackTrace();
       }
       return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> upadateCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateCtageory(requestMap, true)) {
                   Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                   if (!optional.isEmpty()) {
                        categoryDao.save(getCategoryFromMap(requestMap, true));
                        return CafeUtil.getResponsseEntity("Category updated successfly", HttpStatus.OK);
                   } else {
                       return CafeUtil.getResponsseEntity("Category id does not exist in DB",HttpStatus.OK);
                   }
                }
                return CafeUtil.getResponsseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return CafeUtil.getResponsseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponsseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateCtageory(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")){
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        if (isAdd) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }
}
