package com.example.hellospringboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hellospringboot.DAO.UserRepository;
import com.example.hellospringboot.model.Rss;
import com.example.hellospringboot.model.User;
import com.example.hellospringboot.service.BilibiliVideoService;
import com.example.hellospringboot.service.RegService;
import com.example.hellospringboot.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {
    @Autowired
    RegService regService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/reg")
    public String reg(@RequestParam("username") String username, @RequestParam("password") String password) {
        return  regService.reg(username,password);
    }
    @GetMapping("/")
    @CrossOrigin
    public String index(){
        return "helloWorld";
    }
    @GetMapping("/addemail")
    public boolean addsub(@RequestParam("email") String email) throws Exception{
        UserUtils userUtils=new UserUtils();
        UserDetails userDetails=userUtils.getCurrentUser();
        User user=userRepository.findByUsername(userDetails.getUsername());
        if (user==null) return false;
        user.setEmail(email);
        userRepository.save(user);
        return true;
    }
    @GetMapping("/addsub")
    public boolean addsub(@RequestParam("sub") String sub,@RequestParam("type") Integer type) throws Exception{
        UserUtils userUtils=new UserUtils();
        UserDetails userDetails=userUtils.getCurrentUser();
        User user=userRepository.findByUsername(userDetails.getUsername());
        if (user==null) return false;
        ArrayList<String>[] subtype=user.getSublist();
        if (subtype==null) {
            subtype=new ArrayList[3];
        }
        ArrayList<String> sublist=subtype[type];
        if(sublist!=null) {
            for (String s:sublist
                 ) {
                if (s.equals(sub)) return false;
            }
        } else {
            sublist=new ArrayList<>();
        }
        sublist.add(sub);
        subtype[type]=sublist;
        user.setSublist(subtype);
        userRepository.save(user);
        return true;
    }
}