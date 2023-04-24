package com.haidar.coffeemanagementsystem.restImpl;

import com.haidar.coffeemanagementsystem.cofeUtils.CafeUtil;
import com.haidar.coffeemanagementsystem.constatnts.CafeConstants;
import com.haidar.coffeemanagementsystem.models.Bills;
import com.haidar.coffeemanagementsystem.rest.BillRest;
import com.haidar.coffeemanagementsystem.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class BillRestImpl implements BillRest {

    @Autowired
    BillService billService;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {

        try {
            return billService.generateReport(requestMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponsseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bills>> getBills() {
        try {
            return billService.getBills();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try {
            return billService.getPdf(requestMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(String id) {
        try {
            return billService.deleteBill(id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
