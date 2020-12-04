package com.example.hellospringboot.service;

import com.example.hellospringboot.DAO.UserRepository;
import com.example.hellospringboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegService {
    @Autowired
    UserRepository userRepository;
    public String reg( String username,String password){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String encodePassword=encoder.encode(password);
        User user=new User();
        List<User> users= userRepository.findAll();
        for (User u:users
        ) {
            if (u.getUsername().equals(username))
                return "账号已存在!!!!";
        }
        user.setPassword(encodePassword);
        user.setUsername(username);
        user.setRole("USER");
        userRepository.insert(user);
        return "REGOjbK";
    }

}
