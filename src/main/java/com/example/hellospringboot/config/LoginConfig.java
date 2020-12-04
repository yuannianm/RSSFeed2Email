package com.example.hellospringboot.config;

import com.alibaba.fastjson.JSON;
import com.example.hellospringboot.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class LoginConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    LogService logService;
    @Autowired
    LoginSuccess loginSuccess;
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    RoleHierarchy roleHierarchy () {
        RoleHierarchyImpl roleHierarchy=new RoleHierarchyImpl();
        String hierarchy = "DBA > ADMIN > USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(logService);
    }
    @Override
    protected  void  configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests().antMatchers("/reg/**").permitAll() //开放注册
                .antMatchers("/users/**").hasAuthority("ADMIN")
                .anyRequest().permitAll()//.authenticated()//其他任何用户受到验证
                .and().formLogin()
                .loginProcessingUrl("/login").permitAll()  //允许任意用户登录
                .successHandler(loginSuccess.getSucceed())
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        PrintWriter output=httpServletResponse.getWriter();
                        httpServletResponse.setStatus(400);
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("auth",e.getMessage());
                        output.write(JSON.toJSON(map).toString());
                        output.flush();
                        output.close();
                    }
                })
                .and().csrf().disable(); //关闭csrf
    }
}
