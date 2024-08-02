package com.xunhang.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xunhang.mapper.ItemMapper;
import com.xunhang.pojo.entity.Item;
import com.xunhang.service.ItemService;
import com.xunhang.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ItemServiceimpl extends ServiceImpl<ItemMapper,Item> implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private AliOssUtil aliOSSUtils;

    @Override
    public List<Item> getItem(Boolean category, String dateTime, String text, Integer index, Integer count) throws JsonProcessingException {
        return null;
    }

    /*
    * 根据category ,date, text获取失物信息
    */



}
