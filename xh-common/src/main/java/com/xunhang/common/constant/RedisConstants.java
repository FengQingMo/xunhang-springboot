package com.xunhang.common.constant;

public interface RedisConstants {

    String LOGIN_CODE_KEY = "login:code:";

    Long LOGIN_CODE_TTL = 2L;

    /**
     * 分页缓存key （已弃用）
     */
    String IDS_ZSET_KEY = "ids:zset";
    /**
     * 物品信息键 (已弃用）
     */
    String ITEM_KEY = "item:";

    /**
     * 公告
     */
    String ANNOUCEMENT_KEY = "announcement";

    /**
     * 账号登录次数
     */
    String LOGIN_TIMES_KEY = "login:times:";

    /**
     * 账号登录次数时间
     */
    Long LOGIN_TIMES_TTL = 10L;

    /**
     * 账号锁定时间
     */
    Long LOGIN_LOCK_TTL = 5L;

    /**
     * 最大重试次数
     */
   int MAX_ATTEMPTS = 5;

    /**
     * 微信 access_token_key
     */
     String WX_ACCESS_TOKEN_KEY = "accessToken";
}
