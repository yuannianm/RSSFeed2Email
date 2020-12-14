package com.example.hellospringboot;

import com.example.hellospringboot.service.MailService;
import com.example.hellospringboot.service.RSSMailService;
import com.example.hellospringboot.service.RegService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
class HellospringbootApplicationTests {

    @Autowired
    MailService mailService;
    @Autowired
    RSSMailService rssMailService;
    @Test
    void contextLoads() {
        System.out.println(HtmlUtils.htmlEscape("><"));
    }

    @Test
    void scheuld(){
        rssMailService.scanNewFeed();
    }
}
