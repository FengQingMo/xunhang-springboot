create database xunhang;


create table announcement
(
    id           int unsigned auto_increment comment '主键id'
        primary key,
    content      varchar(100) null comment '公告内容',
    publish_date date         null comment '发布时间'
)
    comment '公告表，只读取前三条';

create table item
(
    id           int unsigned auto_increment
        primary key,
    publisher_id varchar(50)   null comment '发布者openid',
    description  varchar(200)  null,
    location     varchar(30)   null comment '地址',
    date         varchar(20)   null,
    category     tinyint(1)    null comment '0:寻找失物,1:寻找失主',
    claimer_id   int default 0 null,
    publish_date varchar(20)   null,
    tag          varchar(300)  null,
    images       varchar(1000) null comment '图片 以逗号分割',
    title        varchar(50)   null
);

create index item_fk1
    on item (publisher_id);

create table user
(
    openid      varchar(50)  not null
        primary key,
    nickname    varchar(20)  null comment '名称',
    phone       varchar(20)  null comment '联系方式',
    password    varchar(20)  null comment '密码',
    image       varchar(300) null comment '头像',
    create_time varchar(20)  not null comment '创建时间',
    update_time varchar(20)  null comment '改变时间',
    constraint id
        unique (openid)
)
    comment '用户表';