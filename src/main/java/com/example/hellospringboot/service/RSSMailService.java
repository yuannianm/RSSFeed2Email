package com.example.hellospringboot.service;

import com.example.hellospringboot.DAO.NewestFeedRepository;
import com.example.hellospringboot.DAO.UserRepository;
import com.example.hellospringboot.model.Mail;
import com.example.hellospringboot.model.NewestFeed;
import com.example.hellospringboot.model.User;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.rss.Enclosure;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jdom2.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Component
@Service


public class RSSMailService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MailService mailService;
    @Autowired
    NewestFeedRepository newestFeedRepository;
    // TODO:不同网站处理规则
    Logger logger= LoggerFactory.getLogger(this.getClass());
    String from="jing.chang@starway-world.cn";

    @Scheduled(fixedRate = 900000 )
    public void scanNewFeed(){
        logger.info("scan feed");
        ArrayList<String>[] sublist=initRssMailServ();
        if (sublist!=null) {
            for (int i = 0; i < 3; i++) {
                if (i < 2) {
                    //bilibili or weibo
                    if (sublist[i] != null) {
                        for (String s : sublist[i]) {
                            try {
                                SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(s)));
                                List<SyndEntry> entries=feed.getEntries();
                                NewestFeed[] newests = newestFeedRepository.findByUrl(s);
                                //如果是新内容
                                if (newests.length==0) {
                                    newests = new NewestFeed[entries.size()];
                                    for (int k=0;k<entries.size();k++){
                                        newests[k]=new NewestFeed();
                                        //    newests[k].setUrl(s);
                                    }
                                }
                                for (SyndEntry e:entries
                                ) {
                                    String content=feed.getTitle();
                                    if (e == null) continue;
                                    //    if (newests.getTitle()==null) newests.setTitle("firstsub"); //第一次订阅
                                    int i1=0;
                                    for (;i1<newests.length;i1++
                                    ) {
                                        if (e.getTitle().equals(newests[i1].getTitle())){
                                            break;
                                        }
                                    }
                                    if (i1==newests.length) {
                                        //推送给订阅了连接的所有用户
                                        NewestFeed addi=new NewestFeed();
                                        addi.setTitle(e.getTitle());
                                        addi.setUrl(s);
                                        newestFeedRepository.save(addi);
                                        String subject = e.getTitle();
                                        content += e.getDescription().getValue() ;
                                        System.out.println(content);
                                        content += e.getLink();
                                        String[] imglist=null;
                                        if (e.getEnclosures() != null) {
                                            imglist = new String[e.getEnclosures().size()];
                                            for (int z = 0; z < e.getEnclosures().size(); z++) {
                                                imglist[z] = e.getEnclosures().get(z).getUrl();

                                            }
                                        }
                                        List<User> users = userRepository.findAll();
                                        for (User u : users
                                        ) {
                                            String to = u.getEmail();
                                            if (to != null) {
                                                if (imglist==null)
                                                    mailService.send(from,to,subject,content);
                                                else
                                                    mailService.sendWithPic(from, to, subject, content, imglist);
                                            } else {
                                                logger.info("未设置邮箱");
                                            }
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
                    //landian or enadget
                    if (sublist[i] != null) {
                        for (String s : sublist[i]) {
                            try {
                                SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(s)));
                                List<SyndEntry> entries=feed.getEntries();
                                NewestFeed[] newests = newestFeedRepository.findByUrl(s);
                                //如果是新内容
                                if (newests.length==0) {
                                    newests = new NewestFeed[entries.size()];
                                for (int k=0;k<entries.size();k++){
                                    newests[k]=new NewestFeed();
                                //    newests[k].setUrl(s);
                                }
                                }
                                for (SyndEntry e:entries
                                ) {
                                    String content=feed.getTitle();
                                    if (e == null) continue;
                                    //    if (newests.getTitle()==null) newests.setTitle("firstsub"); //第一次订阅
                                    int i1=0;
                                    for (;i1<newests.length;i1++
                                         ) {
                                        if (e.getTitle().equals(newests[i1].getTitle())){
                                            break;
                                        }
                                    }
                                    if (i1==newests.length) {
                                        //推送给订阅了连接的所有用户
                                        NewestFeed addi=new NewestFeed();
                                        addi.setTitle(e.getTitle());
                                        addi.setUrl(s);
                                        newestFeedRepository.save(addi);
                                        String subject = e.getTitle();
                                        content += e.getDescription().getValue();
                                        content += e.getLink();
                                        String[] imglist=null;
                                        if (e.getForeignMarkup().get(0) != null) {
                                            try{
                                                imglist = new String[1];
                                                imglist[0] = e.getForeignMarkup().get(0).getAttributes().get(0).getValue();
                                            }catch (Exception exception) {
                                                exception.printStackTrace();
                                                logger.info("缺少属性");
                                            }
                                        }
                                        List<User> users = userRepository.findAll();
                                        for (User u : users
                                        ) {
                                            String to = u.getEmail();
                                            if (to != null) {
                                                if (imglist[0]==null)
                                                    mailService.send(from,to,subject,content);
                                                else
                                                    mailService.sendWithPic(from, to, subject, content, imglist);
                                                logger.info("已发送邮件");
                                            } else {
                                                logger.info("未设置邮箱");
                                            }
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                logger.info("出现错误");
                            }
                        }
                    }
                }
            }
        }
    }
    private ArrayList<String>[] initRssMailServ(){
        List<User> users= userRepository.findAll();
        ArrayList<String>[] sublist=new ArrayList[3];
        for (User u:users
             ) {
            if (u.getSublist()!=null) {
                for (int i = 0; i < 3; i++) {
                    if (u.getSublist()[i] != null) {
                        for (String s : u.getSublist()[i]
                        ) {
                            if (sublist[i] == null) sublist[i] = new ArrayList<>();
                            if (!sublist[i].contains(s))    sublist[i].add(s);
                        }
                    }
                }
            }
        }
        return sublist;
    }
}
