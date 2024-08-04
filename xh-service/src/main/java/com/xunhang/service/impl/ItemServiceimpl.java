package com.xunhang.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhang.common.result.PageResult;
import com.xunhang.common.utils.UserUtil;
import com.xunhang.mapper.ItemMapper;
import com.xunhang.pojo.dto.ItemHomeDTO;
import com.xunhang.pojo.dto.ItemPublishDTO;
import com.xunhang.pojo.dto.WxInfoDTO;
import com.xunhang.pojo.entity.Item;
import com.xunhang.pojo.entity.ItemImage;
import com.xunhang.pojo.vo.ItemVO;
import com.xunhang.service.ItemImageService;
import com.xunhang.service.ItemService;
import com.xunhang.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceimpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    private final ItemImageService itemImageService;
    private final ItemMapper itemMapper;

    @Transactional
    @Override
    public void claimItem(WxInfoDTO wxInfoDTO) {
        Map<String, String> info = new HashMap<>();
        info.put("id", wxInfoDTO.getId().toString());
        info.put("thing1", wxInfoDTO.getThing());
        info.put("name2", wxInfoDTO.getName());
        info.put("phone_number4", wxInfoDTO.getPhone());
        info.put("date3", LocalDateTime.now().toString());
        //todo 认领
        //rabbitTemplate.convertAndSend(Message_EXCHANGE_NAME,Message_ROUTING_KEY_NAME,info);
        update().set("claimer_id", wxInfoDTO.getUserId()).eq("id", wxInfoDTO.getId());
    }

    @Override
    public Map<String, Integer> getBaseData() {
        Long id = UserUtil.getCurrentId();
        int found = 0, lost = 0, unclaimed = 0;
        List<Item> list = query().eq("publisher_id", id).list();
        for (Item item : list) {
            //log.info("item:{}",item);
            if (item.getCategory() == null || item.getIsSuccess() == null)
                continue;
            if (!item.getCategory()) {
                if (!item.getIsSuccess()) {
                    lost += 1;
                } else {
                    found += 1;
                }
            } else {
                if (!item.getIsSuccess()) {
                    unclaimed += 1;
                }
            }
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("found", found);
        map.put("lost", lost);
        map.put("unclaimed", unclaimed);
        return map;
    }

    @Override
    public List<ItemVO> getMyItem(Boolean category) {
        Long id = UserUtil.getCurrentId();
        LambdaQueryWrapper<Item> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Item::getPublisherId, id).eq(Item::getCategory, category).orderByDesc(Item::getUpdateTime);
        List<Item> list = list(wrapper);
        List<ItemVO> itemVOS = new ArrayList<ItemVO>();
        for (Item item : list) {
            ItemVO vo = new ItemVO();
            BeanUtil.copyProperties(item, vo);
            List<ItemImage> itemImages = itemImageService.query().eq("item_id", item.getId()).list();
            List<String> images = new ArrayList<String>();
            itemImages.forEach((itemImage -> images.add(itemImage.getImageUrl())));
            vo.setImages(images);
            itemVOS.add(vo);
        }
        return itemVOS;
    }

    @Override
    @Transactional
    public Boolean publish(ItemPublishDTO itemPublishDTO) {
        log.info("itemHomeDTO:" + itemPublishDTO);
        Long id = UserUtil.getCurrentId();
        //保存物品记录
        Item item = new Item();
        BeanUtil.copyProperties(itemPublishDTO, item);
        item.setPublisherId(id);
        item.setClaimerId(0L);
        item.setIsSuccess(false);
        save(item);
        //保存图片
        Boolean ok = true;
        List<String> images = itemPublishDTO.getImages();
        //if (CollectionUtil.isNotEmpty(images)) {
        //    List<ItemImage> itemImages = new ArrayList<>();
        //    for (String image : images) {
        //        ItemImage itemImage = new ItemImage();
        //        itemImage.setImageUrl(image);
        //        itemImage.setItemId(item.getId());
        //        itemImages.add(itemImage);
        //    }
        //    ok = itemImageService.saveBatch(itemImages);
        //}
        log.info("用户id为:" + id + "的用户发布了一条信息" + "物品信息为为" + item);
        return BooleanUtil.isTrue(ok);
    }

    @SneakyThrows
    @Override
    public PageResult<ItemVO> getHomeItem(ItemHomeDTO itemHomeDTO) {
        CompletableFuture<Long> asyncCount = CompletableFuture.supplyAsync(() -> itemMapper.countHomeItem(itemHomeDTO));
        List<ItemVO> itemVOS = itemMapper.getHomeItem(PageUtil.getLimitCurrent(), PageUtil.getSize(),itemHomeDTO);
        return new PageResult<>(asyncCount.get(),itemVOS);
    }

    @Override
    public void updateItem(Item item) {
        //String itemKey = ITEM_KEY + item.getId();
        saveOrUpdate(item);
        //try {
        //    Boolean lock = redisUtil.tryLock(itemKey);
        //    if (lock) {
        //        stringRedisTemplate.opsForValue().set(itemKey, JSONUtil.toJsonStr(itemChangeDTO));
        //    }
        //} finally {
        //    redisUtil.unlock(itemKey);
        //}
    }

    @Override
    public void deleteItem(Long id, Boolean category) {
    }
}
