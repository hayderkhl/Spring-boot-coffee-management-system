package com.haidar.coffeemanagementsystem.serviceImpl;

import com.haidar.coffeemanagementsystem.cofeUtils.CafeUtil;
import com.haidar.coffeemanagementsystem.constatnts.CafeConstants;
import com.haidar.coffeemanagementsystem.dao.UserDao;
import com.haidar.coffeemanagementsystem.models.User;
import com.haidar.coffeemanagementsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {

        log.info("inside SignUp{}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {

                User user = userDao.findByEmailId(requestMap.get("email"));

                if (Objects.isNull(user)) {

                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtil.getResponsseEntity("successful register", HttpStatus.OK);

                } else {
                    return CafeUtil.getResponsseEntity("Email already exist", HttpStatus.BAD_REQUEST);
                }

            } else {
                return CafeUtil.getResponsseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtil.getResponsseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);

    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
       if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
               && requestMap.containsKey("email") && requestMap.containsKey("password"))
           {
               return  true;
           }
                 return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

}
