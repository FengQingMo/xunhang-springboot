package com.xunhang.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xunhang.pojo.entity.Item;

import java.util.List;

public interface ItemService extends IService<Item> {


    //保存失物信息


    List<Item> getItem(Boolean category, String dateTime, String text,Integer index,Integer count) throws JsonProcessingException;






}
