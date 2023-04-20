package com.haidar.coffeemanagementsystem.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface BillService {
    ResponseEntity<String> generateReport(Map<String, Object> requestMap);
}
