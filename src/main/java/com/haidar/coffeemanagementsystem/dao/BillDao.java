package com.haidar.coffeemanagementsystem.dao;

import com.haidar.coffeemanagementsystem.models.Bills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface BillDao extends JpaRepository<Bills, Integer> {
    @Query("select b from Bills b order by b.uuid desc")
    List<Bills> getAllBills();

    @Query("select b from Bills b where b.createdBy =:username order by b.uuid desc")
    List<Bills> getBillByUser(@Param("username") String currentUser);

    @Transactional
    @Modifying
    @Query("DELETE FROM Bills b WHERE b.uuid = :id")
    void deleteByIdMethod(String id);

    @Query("select b from Bills  b where b.uuid=:id")
    Optional<Bills> findByIdMethod(String id);
}
