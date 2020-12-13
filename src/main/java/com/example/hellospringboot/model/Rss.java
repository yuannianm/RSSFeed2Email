package com.example.hellospringboot.model;


import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class Rss implements Serializable{
    String uid;
    String type;
    Date lastBuildDate;
    JSONArray data;

}
