package com.example.hellospringboot;

import com.example.hellospringboot.controller.RssController;
import com.example.hellospringboot.service.MailService;
import com.example.hellospringboot.service.RSSMailService;

import com.example.hellospringboot.service.WeiBoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

@SpringBootTest
class HellospringbootApplicationTests {

    @Autowired
    WeiBoService weiBoService;
    @Autowired
    RssController rssController;
    @Test
    public void scheuld(){
        System.out.println(rssController.getWeiBoRss("5129369274"));
    }

}
