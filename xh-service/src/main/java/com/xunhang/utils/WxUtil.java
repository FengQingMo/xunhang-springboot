package com.xunhang.utils;

import com.alibaba.fastjson.JSONObject;
import com.xunhang.common.enums.ExceptionEnums;
import com.xunhang.common.exception.GlobalException;
import com.xunhang.common.properties.WeChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.xunhang.common.constant.RedisConstants.WX_ACCESS_TOKEN_KEY;
import static com.xunhang.common.constant.WxConstants.WX_ACCESS_TOKEN_URL;

@Component
public class WxUtil {

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void getAccessToken(){
        String appid = weChatProperties.getAppid();
        String secret = weChatProperties.getSecret();
        String grant_type = "client_credential";
        Map<String,String>map = new HashMap<String,String>();
        map.put("appid",appid);
        map.put("secret",secret);
        map.put("grant_type",grant_type);
        String json = HttpClientUtil.doGet(WX_ACCESS_TOKEN_URL,map);
        JSONObject jsonObject =JSONObject.parseObject(json);
        Integer errcode = jsonObject.getInteger("errcode");
        if (errcode == null || errcode == 0) {
            String accessToken = jsonObject.getString("access_token");
            Integer expiresIn = jsonObject.getInteger("expires_in");
            stringRedisTemplate.opsForValue().set(WX_ACCESS_TOKEN_KEY,accessToken,expiresIn, TimeUnit.SECONDS);
        } else {
            throw new GlobalException(ExceptionEnums.WX_ACCESSTOKEN_ERROR);
        }
    }


    public void sendSubscribeInfo(){

    }


}
