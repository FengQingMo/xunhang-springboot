# 寻航

## 项目描述

本项目基于[拾领星球开源版: 拾领星球(大学校园失物招领寻物启事小程序) (gitee.com)](https://gitee.com/sbuds/lost-planet-opend)二次开发，原项目使用微信小程序云开发作为后端，现使用Spring Boot开发，并集成  [盒子IM](https://gitee.com/bluexsx/box-im) 私聊部分实现简单的聊天功能方便用户交流，前端基于 uniapp。

本项目是用于帮助用户找回丢失物品的校园失物招领平台

## 效果图

![大厅](%E5%A4%A7%E5%8E%85.png)
![首页](https://minio.fengqingmo.top/xunhang/headImage/20240807/1723038063410.png)


## 后端框架

- 基础框架: SpringBoot
- ORM框架：mybatisplus
- 缓存中间件：redis
- 对象存储：minio/阿里云oss

## 本地启动

### 前端

使用uniapp导入前端项目，并将appid换成自己的，然后选择在微信小程序运行即可。

在**env.js**文件中切换环境，默认是访问我已经部署在服务器的后端

### 后端

运行环境

- mysql 8.0 运行sql文件 修改账号密码

- jdk8

- minio

  运行**xh-service**下的启动类及 **im-server**的启动类

##  点下star 感谢喵

## 后续计划

1. 使用 **RabbitMQ**实现用户认领失物以及发送信息时通知对方
2. 使用 **ElasticSearch**作为大厅搜索引擎
3. 集成后台管理系统

（ but不会写前端 O.o )