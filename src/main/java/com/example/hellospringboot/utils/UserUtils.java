package com.example.hellospringboot.utils;

import com.example.hellospringboot.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserUtils {
    public UserDetails getCurrentUser(){
        //返回当前对象
        return  (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
