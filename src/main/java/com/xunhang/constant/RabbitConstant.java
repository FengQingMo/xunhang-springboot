package com.xunhang.constant;

public class RabbitConstant {

    public static final Integer DELIVERING = 0;//消息投递中
    public static final Integer SUCCESS = 1;//消息投递成功
    public static final Integer FAILURE = 2;//消息投递失败
    public static final Integer MAX_TRY_COUNT = 3;//最大重试次数
    public static final Integer MSG_TIMEOUT = 1;//消息超时时间
    public static final String Message_QUEUE_NAME = "message.queue";
    public static final String Message_EXCHANGE_NAME = "message.exchange";
    public static final String Message_ROUTING_KEY_NAME = "message.routing.key";
}
