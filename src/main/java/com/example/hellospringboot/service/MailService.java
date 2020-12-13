package com.example.hellospringboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sun.misc.BASE64Encoder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

@Component
public class MailService {
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    JavaMailSender javaMailSender;

    public void send(String from,String to,String subject,String content){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); //true表示HTML格式
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            javaMailSender.send(message);
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }
    public void sendWithPic(String from,String to,String subject,String content,String[] imgList){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); //true表示HTML格式
            Context context = new Context();
            String[] cid = new String[imgList.length];
            for (int i=0;i<cid.length;i++
                 ) {
                cid[i]= UUID.randomUUID().toString();
            }
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            context.setVariable("subject", subject);
            context.setVariable("content", content);
            context.setVariable("cids", cid);
            helper.setText(templateEngine.process("MailTemplate.html", context),true);
            for (int i = 0; i < imgList.length; i++
            ) {
                download(imgList[i],i+".jpg","./tmp/");
                FileSystemResource resource = new FileSystemResource(new File("./tmp/"+i+".jpg"));
                helper.addInline(cid[i], resource);
            }
            javaMailSender.send(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void download(String urlString, String filename,String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath()+"/"+filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }
}
