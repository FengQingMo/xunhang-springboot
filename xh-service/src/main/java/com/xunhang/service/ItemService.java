package com.xunhang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunhang.common.result.PageResult;
import com.xunhang.pojo.dto.ItemHomeDTO;
import com.xunhang.pojo.dto.ItemPublishDTO;
import com.xunhang.pojo.dto.WxInfoDTO;
import com.xunhang.pojo.entity.Item;
import com.xunhang.pojo.vo.ItemVO;

import java.util.List;
import java.util.Map;

public interface ItemService extends IService<Item> {

    /**
     * 获取首页的三个物品信息
     */
    Map<String, Integer> getBaseData();

    /**
     * 获取自己发布的物品信息
     * @param category 分类
     */
    List<ItemVO> getMyItem(Boolean category);

    /**
     * 发布物品信息
     */
    void publish(ItemPublishDTO itemPublishDTO);

    /**
     * 获取首页物品信息
     */
    PageResult<ItemVO> getHomeItem(ItemHomeDTO itemHomeDTO);

    /**
     * 修改物品
     */
    void updateItem(Item item);

    /**
     * 删除物品
     */
    void deleteItem(Long id, Boolean category);

    /**
     * 认领物品
     */
    void claimItem(WxInfoDTO wxInfoDTO);
}
