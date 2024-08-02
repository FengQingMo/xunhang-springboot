package com.xunhang.controller.user;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xunhang.pojo.entity.Message;
import com.xunhang.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.xunhang.constant.RedisConstants.MESSAGE_QUEUE_KEY;

/**
 * @author websocket服务
 */
@ServerEndpoint(value = "/websocket/{id}")
@Component
public class WebSocketController {
    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
    /**
     * 接收userId
     */
    private Long id;


    private static RabbitTemplate rabbitTemplate;

    private static StringRedisTemplate redisTemplate;

    private static ChatService chatService;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        WebSocketController.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setWebSocketService(ChatService webSocketService) {
        WebSocketController.chatService = webSocketService;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        WebSocketController.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 逻辑处理
     */

    public static Map<Long, Session> sessionMap = new ConcurrentHashMap<>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") Long id) {
        this.id = id;

        if (!sessionMap.containsKey(id)) {
            sessionMap.put(id, session);
            log.info("有新用户加入，userid={}, 当前在线人数为：{}", id, sessionMap.size());
            //JSONObject result = new JSONObject();


//        {"users": [{"username": "zhang"},{ "username": "admin"}]}

        }


    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(id);

        log.info("有一连接关闭，移除userid={}的用户session, 当前在线人数为：{}", id, sessionMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {

        log.info("服务端收到用户userid={}的消息:{}", id, message);
        JSONObject obj = JSONUtil.parseObj(message);
        if (Objects.equals(obj.getStr("type"), "ping")) {
            //return Result.success("live");
        } else {
            Long receiveId = obj.getLong("toUserId"); // to表示发送给哪个用户，比如 admin
            String content = obj.getStr("content"); // 发送的消息文本  hello
            // {"to": "admin", "content": "聊天文本"}
            Message message1 = new Message();
            message1.setContent(content);
            message1.setFromUserId(id);
            message1.setToUserId(receiveId);

            if (sessionMap.get(receiveId) != null) {
                message1.setIsRead(true);
                // 服务器端 再把消息组装一下，组装后的消息包含发送人和发送的文本内容
                // {"sendId": "zhang", "content": "hello"}
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sendId", id);  // sendId 是 zhang
                jsonObject.put("content", content);  // content 同上面的text
                jsonObject.put("sendTime", LocalDateTime.now());
                Session toSession = sessionMap.get(receiveId);
                this.sendMessage(jsonObject.toString(), toSession);
                log.info("发送给用户userid={}，消息：{}", receiveId, jsonObject);
            } else {
                String messageKey = MESSAGE_QUEUE_KEY + receiveId;
                long currentTimeMillis = System.currentTimeMillis();
                redisTemplate.opsForZSet().add(messageKey, JSONUtil.toJsonStr(message1), currentTimeMillis);
                //String message2 = "Hello, RabbitMQ!";
                //rabbitTemplate.convertAndSend(Message_EXCHANGE_NAME,Message_ROUTING_KEY_NAME,message2);
            }

            chatService.isFirstChat(id, receiveId, content);
            //return Result.success();
        }


    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}
