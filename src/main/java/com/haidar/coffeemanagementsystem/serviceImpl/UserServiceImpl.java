package com.haidar.coffeemanagementsystem.serviceImpl;

import com.google.common.base.Strings;
import com.haidar.coffeemanagementsystem.cofeUtils.CafeUtil;
import com.haidar.coffeemanagementsystem.cofeUtils.EmailUtils;
import com.haidar.coffeemanagementsystem.constatnts.CafeConstants;
import com.haidar.coffeemanagementsystem.dao.UserDao;
import com.haidar.coffeemanagementsystem.jwt.CustomerUserDetailsService;
import com.haidar.coffeemanagementsystem.jwt.JwtFilter;
import com.haidar.coffeemanagementsystem.jwt.JwtUtil;
import com.haidar.coffeemanagementsystem.models.User;
import com.haidar.coffeemanagementsystem.service.UserService;
import com.haidar.coffeemanagementsystem.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    EmailUtils emailUtils;

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

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {

     log.info("Inside login");
     try {
         Authentication auth = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
         );
         if(auth.isAuthenticated()) {
             if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                 return new ResponseEntity<String>("{\"token\":\""+jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                         customerUserDetailsService.getUserDetail().getRole())+"\"}", HttpStatus.OK);
             }
             else {
                 return new ResponseEntity<String>("{\"message\":\"" + "wait for admin approval"+"\"}", HttpStatus.BAD_REQUEST);
             }
         }

     } catch (Exception ex) {
         log.error("{}", ex);
     }
        return new ResponseEntity<String>("{\"message\":\"" + "Bad Credentials"+"\"}",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if (jwtFilter.isAdmin()) {

                Optional<User> optionalUser = userDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!optionalUser.isEmpty()) {
                    userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), optionalUser.get().getEmail(), userDao.getAllAdmin());
                    return CafeUtil.getResponsseEntity("User Status Uptated Successfly", HttpStatus.OK);

                } else {
                   return CafeUtil.getResponsseEntity("User Id doesn't exist", HttpStatus.OK);
                }
            } else {
                return CafeUtil.getResponsseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtil.getResponsseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //we gonna use this to check the token when we pass from a page to another
    //to let our application more safe
    @Override
    public ResponseEntity<String> checkToken() {
       return CafeUtil.getResponsseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (!userObj.equals(null)) {
                if (userObj.getPassword().equals(requestMap.get("oldPassword"))){
                    userObj.setPassword(requestMap.get("newPassword"));
                    userDao.save(userObj);
                    return CafeUtil.getResponsseEntity("Passwoed Updated Successfly", HttpStatus.OK);
                }
                return CafeUtil.getResponsseEntity("Incorrect old Passwwoed", HttpStatus.BAD_REQUEST);
            }
            return CafeUtil.getResponsseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtil.getResponsseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userDao.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgotMail(user.getEmail(),"Credentials by cafe management system.", user.getPassword());
                return CafeUtil.getResponsseEntity("check your email for Credentials.", HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtil.getResponsseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if (status!=null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approuved", "USER:-"+ user + "\n is approved by \n ADMIN:-"+ jwtFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Desabled", "USER:-"+ user + "\n is disabled by \n ADMIN:-"+ jwtFilter.getCurrentUser(), allAdmin);
        }
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
