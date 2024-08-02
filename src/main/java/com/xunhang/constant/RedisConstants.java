package com.xunhang.constant;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;

    public static final String IDS_ZSET_KEY = "ids:zset";

    public static final String ITEM_KEY = "item:";

    public static final String MESSAGE_QUEUE_KEY = "messageQueue:";

    public static final Long ITEM_INFO_TTL = 60L;
    public static String constructCacheKey(Boolean category, Integer index, Integer count) {
        // 根据参数构造缓存键
        return IDS_ZSET_KEY + category + "page:" + index + " limit:" + count;
    }
}
