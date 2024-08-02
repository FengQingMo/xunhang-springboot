package com.xunhang;

import com.xunhang.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XunhangApplicationTests {

    @Autowired
    private ItemService itemService;

    @Test
    public void test(){
        // 获取当前时间的毫秒数
        long currentTimeMillis = System.currentTimeMillis();

        System.out.println("当前时间的毫秒数：" + currentTimeMillis);
    }


}




    /*
    * 测试 java http
    * */







