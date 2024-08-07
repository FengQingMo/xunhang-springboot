package com.xunhang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhang.common.result.PageResult;
import com.xunhang.common.utils.UserUtil;
import com.xunhang.mapper.ItemImageMapper;
import com.xunhang.mapper.ItemMapper;
import com.xunhang.mapper.UserMapper;
import com.xunhang.pojo.dto.ItemHomeDTO;
import com.xunhang.pojo.dto.ItemPublishDTO;
import com.xunhang.pojo.dto.WxInfoDTO;
import com.xunhang.pojo.entity.Item;
import com.xunhang.pojo.entity.ItemImage;
import com.xunhang.pojo.entity.User;
import com.xunhang.pojo.vo.ItemVO;
import com.xunhang.pojo.vo.UserVO;
import com.xunhang.service.ItemService;
import com.xunhang.utils.BeanUtils;
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

    private final ItemMapper itemMapper;

    private final ItemImageMapper itemImageMapper;

    private final UserMapper userMapper;

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
        List<ItemVO> itemVOS = itemMapper.getMyItem(id, category);
        User user = userMapper.selectById(id);
        UserVO userVO = BeanUtils.copyProperties(user, UserVO.class);
        itemVOS.forEach(itemVO -> itemVO.setUserVO(userVO));
        return itemVOS;
    }

    @Override
    @Transactional
    public void publish(ItemPublishDTO itemPublishDTO) {
        log.info("itemHomeDTO:" + itemPublishDTO);
        Long userId = UserUtil.getCurrentId();
        //保存物品记录
        Item item = BeanUtils.copyProperties(itemPublishDTO, Item.class);
        item.setPublisherId(userId);
        item.setClaimerId(0L);
        save(item);
        //保存图片
        List<String> images = itemPublishDTO.getImages();
        List<ItemImage> itemImageList = new ArrayList<>(images.size());
        for (String image : images) {
            ItemImage itemImage = new ItemImage(image, item.getId());
            itemImageList.add(itemImage);
        }
        itemImageMapper.insertList(itemImageList);
        log.info("用户id为:" + userId + "的用户发布了一条信息" + "物品信息为" + item);
    }

    @SneakyThrows
    @Override
    public PageResult<ItemVO> getHomeItem(ItemHomeDTO itemHomeDTO) {
        CompletableFuture<Long> asyncCount = CompletableFuture.supplyAsync(() -> itemMapper.countHomeItem(itemHomeDTO));
        List<ItemVO> itemVOS = itemMapper.getHomeItem(PageUtil.getLimitCurrent(), PageUtil.getSize(), itemHomeDTO);
        return new PageResult<>(asyncCount.get(), itemVOS);
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
