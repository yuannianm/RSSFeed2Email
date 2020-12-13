package com.example.hellospringboot;

import com.example.hellospringboot.service.MailService;
import com.example.hellospringboot.service.RegService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
class HellospringbootApplicationTests {

    @Autowired
    MailService mailService;
    @Test
    void contextLoads() {
        String[] list={"https://wx1.sinaimg.cn/orj360/005XX5y4gy1ghwak3x19xj31ks09ujti.jpg","https://wx1.sinaimg.cn/orj360/005XX5y4gy1ghwak3x19xj31ks09ujti.jpg"};
        mailService.sendWithPic("27729148@qq.com","yuannianm@hotmail.com","subject","content:",list);
    }
}
