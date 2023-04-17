package com.haidar.coffeemanagementsystem.dao;

import com.haidar.coffeemanagementsystem.models.Product;
import com.haidar.coffeemanagementsystem.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {


    @Query("select new com.haidar.coffeemanagementsystem.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.category.id,p.category.name , p.status) from Product p where p.status='true' ")
    List<ProductWrapper> getAllProduct();

    @Modifying
    @Transactional
    @Query("update Product p set p.status=:status where p.id=:id")
    Integer updateProductStatus(@Param("status") String status,@Param("id") int id);
}
