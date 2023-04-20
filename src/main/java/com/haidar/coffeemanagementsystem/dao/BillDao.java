package com.haidar.coffeemanagementsystem.dao;

import com.haidar.coffeemanagementsystem.models.Bills;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillDao extends JpaRepository<Bills, Integer> {
}
