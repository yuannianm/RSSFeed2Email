package com.example.hellospringboot.model;


import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Rss implements Serializable{
    String uid;
    String type;
    Date lastBuildDate;
    JSONArray data;
}
