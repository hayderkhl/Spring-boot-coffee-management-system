package com.haidar.coffeemanagementsystem.dao;

import com.haidar.coffeemanagementsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDao extends JpaRepository<User, Integer> {

    //the name of the table in the query should be written like the name of class
    @Query("select u from User u where u.email=:email")
    User findByEmailId(@Param("email") String email);
}
