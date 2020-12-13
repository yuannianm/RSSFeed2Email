package com.example.hellospringboot.model;


import lombok.Data;

@Data
public class Mail {
    String from;
    String to;
    String subject;
    String content;
    String[] imgList;
}
