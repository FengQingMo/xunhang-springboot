package com.xunhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunhang.pojo.dto.ItemHomeDTO;
import com.xunhang.pojo.entity.Item;
import com.xunhang.pojo.vo.ItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper extends BaseMapper<Item> {

    List<Item> getItemByPublisherId(@Param("publisherId") String publisherId, @Param("category") Boolean category);

    List<ItemVO> getHomeItem(@Param("current") Long current, @Param("size") Long size,@Param("itemHomeDTO") ItemHomeDTO itemHomeDTO);

    Long countHomeItem(@Param("itemHomeDTO") ItemHomeDTO itemHomeDTO);

    List<String> getItemImagesByItemId(@Param("itemId") String itemId);

    List<ItemVO> getMyItem(@Param("id") Long id, @Param("category") Boolean category);
}
