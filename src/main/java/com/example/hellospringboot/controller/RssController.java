package com.example.hellospringboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hellospringboot.model.Rss;
import com.example.hellospringboot.service.BilibiliVideoService;
import com.example.hellospringboot.service.WeiBoService;
import com.rometools.rome.feed.rss.*;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;



import javax.xml.crypto.dsig.XMLObject;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rss")
public class RssController {
    @Autowired
    BilibiliVideoService bilibiliVideoService;
    @Autowired
    WeiBoService weiBoService;
    @GetMapping(value = "/proxy", produces ={MediaType.APPLICATION_XML_VALUE} )
    @CrossOrigin
    public String  proxy(@RequestParam("url") String url) throws Exception{
        RestTemplate restTemplate=new RestTemplate();
        return restTemplate.getForObject(URLDecoder.decode(url,"UTF-8") ,String.class);
    }
    @GetMapping("/bilibili")
    @CrossOrigin
    public Channel getBilibiliRss(@RequestParam("uid") String uid){
        //获取数据
        Rss bilibili=bilibiliVideoService.getVideo(uid);
        JSONArray rss=bilibili.getData();
        //RSS with Rome
        //Channel
        Channel channel=new Channel();
        channel.setFeedType("rss_2.0");
        channel.setTitle(rss.getJSONObject(0).getString("author"));
        channel.setDescription(bilibili.getUid());
        channel.setLink("https://space.bilibili.com/"+bilibili.getUid());
        channel.setLanguage("zh-cn");
        channel.setPubDate(bilibili.getLastBuildDate());
        //Items
        ArrayList<Item> items=new ArrayList<>();
        for (int i = 0; i < rss.size(); i++) {
            Item item = new Item();
            JSONObject data=rss.getJSONObject(i);
            item.setAuthor(data.getString("author"));
            item.setTitle(data.getString("title"));
            Description description=new Description();
        //    description.setValue(data.getString("description"));
            description.setValue(data.getString("description")+"<br><iframe width=70% height=500px src=https://player.bilibili.com/player.html?aid="+data.getString("aid")+"></iframe>");
            item.setDescription(description);
            //图片&视频
            List enclosures=new ArrayList();
            Enclosure pic=new Enclosure();
            pic.setType("image");
            pic.setUrl("https:"+data.getString("pic"));
            enclosures.add(pic);
            item.setEnclosures(enclosures);
            //发布时间
            Date date=new Date();
            date.setTime(data.getLong("created")*1000);
            item.setPubDate(date);
            //原链接
            item.setLink("https://www.bilibili.com/video/"+data.getString("bvid"));
            Guid guid=new Guid();
            guid.setPermaLink(false);
            guid.setValue("https://www.bilibili.com/video/"+data.getString("bvid"));
            item.setGuid(guid);
            items.add(item);
        }
        channel.setItems(items);
        return channel;
    }
    @GetMapping("/weibo")
    @CrossOrigin
    public Object getWeiBoRss(@RequestParam("uid") String uid){
        Rss weiBo=weiBoService.getWeiBo(uid);
        JSONArray rss=weiBo.getData();

        Channel channel=new Channel();
        channel.setFeedType("rss_2.0");
        channel.setTitle(rss.getJSONObject(1).getJSONObject("mblog").getJSONObject("user").getString("screen_name"));
        channel.setDescription(weiBo.getUid());
        channel.setLink("https://weibo.com/"+weiBo.getUid());
        channel.setLanguage("zh-cn");
        channel.setPubDate(weiBo.getLastBuildDate());
        //Items
        ArrayList<Item> items=new ArrayList<>();
        for (int i = 0; i < rss.size(); i++) {
            Item item = new Item();
            JSONObject data=rss.getJSONObject(i).getJSONObject("mblog");
            if (data==null) continue;
            item.setAuthor(data.getJSONObject("user").getString("screen_name") );
            /*
            if (data.getString("text").length()>20)
                item.setTitle(data.getString("text").substring(0,19));
            else
                item.setTitle(data.getString("text"));
             */
            item.setTitle(data.getJSONObject("user").getString("screen_name") + "发了新微博");
            Description description=new Description();
            StringBuilder quote=new StringBuilder();
            if (data.getJSONObject("retweeted_status")!= null){
                JSONObject user=data.getJSONObject("retweeted_status").getJSONObject("user");
                if (data.getJSONObject("retweeted_status").getJSONObject("user")!=null) {
                    quote.append("<div style=\"border-left: 3px solid gray; padding-left: 1em;\">")
                            .append("转发 <a href=https://weibo.com/")
                            .append(user.getString("id"))
                            .append(" target=\"_blank\">@")
                            .append(user.getString("screen_name"))
                            .append("</a>").append(data.getJSONObject("retweeted_status").getString("text"))
                            .append("</div>");
                }
            }
            description.setValue(data.getString("text")+quote.toString());
            item.setDescription(description);
            //图片&视频
            List enclosures=new ArrayList();
            if (data.getJSONArray("pics")!=null){
                JSONArray pics=data.getJSONArray("pics");
            for (int j = 0; j < pics.size(); j++) {
                Enclosure pic=new Enclosure();
                pic.setType("image");
                pic.setUrl(pics.getJSONObject(j).getString("url"));

                enclosures.add(pic);
            }
            }
            item.setEnclosures(enclosures);
            //发布时间
            try{
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=new Date();
                date=format.parse(data.getString("created_at"));
                item.setPubDate(date);
            } catch (ParseException e){
                e.getErrorOffset();
            }

            //原链接
            item.setLink("https://m.weibo.cn/5466550668/"+data.getString("bid"));
            Guid guid=new Guid();
            guid.setPermaLink(false);
            guid.setValue("https://m.weibo.cn/5466550668/"+data.getString("bid"));
            item.setGuid(guid);
            items.add(item);
        }
        channel.setItems(items);
        return channel;
    }
}
