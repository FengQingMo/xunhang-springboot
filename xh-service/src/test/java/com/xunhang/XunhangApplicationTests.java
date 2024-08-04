package com.xunhang;

import com.alibaba.fastjson.JSON;
import com.xunhang.common.properties.JwtProperties;
import com.xunhang.common.utils.JwtUtil;
import com.xunhang.session.UserSession;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {XunhangApplication.class})
@RunWith(SpringRunner.class)
class XunhangApplicationTests {


    JwtProperties jwtProperties;

    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;


    //@Test
    //public void test() {
    //    WxInfoDTO wxInfoDTO = new WxInfoDTO(28L, "og86F5H4reYY4kDCH0M8YlwGsi0Q", 1L, false, "耳机", "雪净痕", "18607048856", LocalDate.now());
    //    rabbitTemplate.convertAndSend(RabbitConstant.Message_EXCHANGE_NAME, RabbitConstant.Message_ROUTING_KEY_NAME, JSONUtil.toJsonStr(wxInfoDTO));
    //}

    @Test
    // 生成accesstoken
    public void acccesstest() {
        UserSession session = new UserSession();
        session.setNickname("雪净痕");
        session.setUsername("雪净痕");
        session.setTerminal(0);
        session.setUserId(2L);
        String strJson = JSON.toJSONString(session);
        String accessToken = JwtUtil.sign(2L, strJson, jwtProperties.getUserTtl(), jwtProperties.getUserSecretKey());
        System.out.println(accessToken);
    }

    /**
     * 获取加密的密码
     */
    @Test
    public void getEncodePassword() {
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);
    }

}







