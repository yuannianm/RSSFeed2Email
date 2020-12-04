package com.example.hellospringboot.controller;

import com.example.hellospringboot.model.Rss;
import com.example.hellospringboot.service.BilibiliVideoService;
import com.example.hellospringboot.service.RegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    @Autowired
    RegService regService;

    @GetMapping("/reg")
    public String reg(@RequestParam("username") String username, @RequestParam("password") String password) {

        return  regService.reg(username,password);
    }
    @GetMapping("/")
    @CrossOrigin
    public String index(){
        return "helloWorld";
    }

}