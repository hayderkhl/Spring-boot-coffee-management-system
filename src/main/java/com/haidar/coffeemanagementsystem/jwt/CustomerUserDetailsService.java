package com.haidar.coffeemanagementsystem.jwt;

import com.haidar.coffeemanagementsystem.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;
    private com.haidar.coffeemanagementsystem.models.User userDetail;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("inside loadUserByUsername {}", username);
        userDetail = userDao.findByEmailId(username);
        if (!Objects.isNull(username)) {
            return new User(userDetail.getEmail(),userDetail.getPassword(), new ArrayList<>());
        } else
            throw new UsernameNotFoundException("User not found");
    }

    public com.haidar.coffeemanagementsystem.models.User getUserDetail() {
        return userDetail;
    }

}
