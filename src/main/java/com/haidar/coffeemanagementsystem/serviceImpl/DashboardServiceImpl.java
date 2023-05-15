package com.haidar.coffeemanagementsystem.serviceImpl;

import com.haidar.coffeemanagementsystem.dao.BillDao;
import com.haidar.coffeemanagementsystem.dao.CategoryDao;
import com.haidar.coffeemanagementsystem.dao.ProductDao;
import com.haidar.coffeemanagementsystem.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    BillDao billDao;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("category" , categoryDao.count());
        map.put("product", productDao.count());
        map.put("bills", billDao.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
