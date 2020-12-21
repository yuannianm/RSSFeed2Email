package com.example.hellospringboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
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

    public boolean send(String from,String to,String subject,String content){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); //true表示HTML格式
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            javaMailSender.send(message);
            return true;
        }catch (MessagingException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean sendWithPic(String from,String to,String subject,String content,String[] imgList){
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
            context.setVariable("content", content );
            context.setVariable("cids", cid);
            helper.setText(templateEngine.process("MailTemplate.html", context ),true);
            for (int i = 0; i < imgList.length; i++
            ) {
                download(imgList[i],i+".jpg","./tmp/");
                FileSystemResource resource = new FileSystemResource(new File("./tmp/"+i+".jpg"));
                helper.addInline(cid[i], resource);
            }
            javaMailSender.send(message);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private static void download(String urlString, String filename,String savePath) throws Exception {
        // 构造URL
        Logger logger= LoggerFactory.getLogger(MailService.class);
        logger.info(urlString);
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(60*1000);
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
