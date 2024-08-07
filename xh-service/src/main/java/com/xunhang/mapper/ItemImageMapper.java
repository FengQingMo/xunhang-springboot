package com.xunhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunhang.pojo.entity.ItemImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemImageMapper extends BaseMapper<ItemImage> {

    void insertList(List<ItemImage> itemImageList);
}