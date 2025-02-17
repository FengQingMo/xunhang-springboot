package com.bx.imcommon.contant;

public final class IMRedisKey {

    private IMRedisKey() {}

    /**
     * im-server最大id,从0开始递增
     */
    public static final String  IM_MAX_SERVER_ID = "im:max_server_id";
    /**
     * 用户ID所连接的IM-server的ID
     */
    public static final String  IM_USER_SERVER_ID = "im:user:server_id";
    /**
     * 未读私聊消息队列
     */
    public static final String IM_MESSAGE_PRIVATE_QUEUE = "im:message:private";
    /**
     * 未读群聊消息队列
     */
    public static final String IM_MESSAGE_GROUP_QUEUE = "im:message:group";
    /**
     * 私聊消息发送结果队列
     */
    public static final String IM_RESULT_PRIVATE_QUEUE = "im:result:private";
    /**
     * 群聊消息发送结果队列
     */
    public static final String IM_RESULT_GROUP_QUEUE = "im:result:group";


    /**
     *  获取用户所在的服务器serverid key
     *
     * @param userId
     * @param terminal
     * @return
     */
    public static String getUserServerKey(Long userId, Integer terminal) {
        return IM_USER_SERVER_ID + userId + '-' + terminal;
    }

    /**
     * 通过redis的通配符判断有没有这个key
     *
     * @param userId
     * @return
     */
    public static String getUserOnlineKey(Long userId){
         return IM_USER_SERVER_ID + userId + '*';
    }

}
