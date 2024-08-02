package com.xunhang.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunhang.pojo.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper extends BaseMapper<Item> {

    
    
    void save(Item item);

    List<Item> getItem(@Param("category") Boolean category, @Param("date") String date, @Param("text") String text,@Param("index") Integer index,@Param("count") Integer count);


    List<Item> getItemByPublisherId(@Param("publisherId") String publisherId, @Param("category") Boolean category);
}
