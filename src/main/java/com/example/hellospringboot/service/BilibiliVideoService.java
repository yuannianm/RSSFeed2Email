package com.example.hellospringboot.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hellospringboot.model.Rss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BilibiliVideoService {

    @Cacheable(cacheNames = "rss_cache")
    public Rss getVideo(String uid){
        RestTemplate restTemplate=new RestTemplate();
        JSONArray list= restTemplate.getForObject("https://api.bilibili.com/x/space/arc/search?mid="+uid+"&ps=10&tid=0&pn=1&order=pubdate&jsonp=jsonp", JSONObject.class).getJSONObject("data").getJSONObject("list").getJSONArray("vlist");
        Date date=new Date();
        Rss bilibili=new Rss(uid,"bilibili",date,list);
        System.out.println("无缓存调用");
        return bilibili;
    }
}
