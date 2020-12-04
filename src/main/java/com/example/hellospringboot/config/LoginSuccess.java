package com.example.hellospringboot.config;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
@Configuration
public class LoginSuccess {
    public AuthenticationSuccessHandler getSucceed(){
         return (HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,Authentication authentication)-> {
                PrintWriter output=httpServletResponse.getWriter();
                UserDetails principal=(UserDetails) authentication.getPrincipal();
                httpServletResponse.setStatus(200);
                HashMap<String,Object> map=new HashMap<>();
                map.put("status",200);
                map.put("userinfo",principal);
            //    ObjectMapper objectMapper=new ObjectMapper();  //JACKSON转换对象为JSON
                output.write(JSON.toJSON(map).toString());
                output.flush();
                output.close();
            };
        };
    }
