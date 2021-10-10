基于SpringBoot 抓取各种平台信息为RSS,并发送订阅通知到邮箱

目前支持抓取微博,B站视频为RSS格式

可以发送最新的订阅内容到邮箱,订阅链接可以是其他网站提供[如无法获取尝试使用**代理抓取FEED**]

# 部署方法

1. 拉取最新代码
2. 部署自己的MongoDb,Redis,修改源代码/src/main/resources/application.properties中的mongodb和redis地址和密码等信息
3. 注册一个腾讯企业邮箱,或者其他支持SMTP的邮箱,同样把账号密码更新到application.properties
4. 打包部署到自己的服务器

## EMAIL服务使用流程

1. 注册并登录用户
2. 添加email
3. 添加订阅

# 功能

| 功能         | 备注              | 完成 |
| ------------ | ----------------- | ------ |
| 抓取B站      |                   | DONE   |
| 抓取微博     |                   | DONE   |
| 代理抓取FEED |                   | DONE   |
| 用户订阅列表 |                   | DONE   |
| 用户登录     |                   | DONE   |
| 邮件发送     |          | DONE   |

​    



# 接口



#### 注册

API:/reg?{username}&{password}

#### 登录

API:/login

#### Bilibili

API:/rss/bilibili?{uid}

#### 微博

API:/rss/weibo?{uid}

#### 代理其他Feed生成FEED

API:/rss/proxy?{uid}

#### 添加订阅

API:/addsub?{sub}&{type}

```
数组大小为3,0--bilibili,1--weibo,2--others


//例子
微博 http://localhost:8080/addsub?sub=http%3a%2f%2flocalhost%3a8080%2frss%2fweibo%3fuid%3d5129369274&type=1
```

#### 添加email

```
/addemail
```



> 相关:RSSHub,Weibo-Rss
