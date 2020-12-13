package com.example.hellospringboot.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class NewestFeed {
    ObjectId id;
    String url;
    String title;

}
