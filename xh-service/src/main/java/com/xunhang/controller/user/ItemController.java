package com.xunhang.controller.user;

import com.xunhang.common.result.Result;
import com.xunhang.common.result.ResultUtils;
import com.xunhang.pojo.dto.ItemHomeDTO;
import com.xunhang.pojo.dto.ItemPublishDTO;
import com.xunhang.pojo.dto.WxInfoDTO;
import com.xunhang.pojo.entity.Item;
import com.xunhang.service.ItemService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author 风清默
 * @date 2024/8/3 14:53
 * @description: 物品控制器层
 */
@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 获取用户界面的物品数据
     */
    @GetMapping("/getMyItemData")
    @ApiOperation("获取用户界面的物品数据")
    public Result getBaseData() {
        return ResultUtils.success(itemService.getBaseData());
    }

    /**
     * 通过分类获取当前用户的发布信息
     *
     * @param category 分类
     */
    @ApiOperation("获取当前用户的发布信息")
    @GetMapping("/getMyItem/{category}")
    public Result getMyItem(@PathVariable Boolean category) {
        return ResultUtils.success(itemService.getMyItem(category));
    }

    /**
     * 发布失物
     *
     * @param itemPublishDTO
     */
    @PostMapping("/publishItem")
    @ApiOperation(value = "发布物品信息")
    public Result publish(@RequestBody @Valid ItemPublishDTO itemPublishDTO) {
        itemService.publish(itemPublishDTO);
        return ResultUtils.success("发布成功");
    }

    /**
     * 获取首页的物品信息
     */
    @PostMapping("/getHomeItem")
    @ApiOperation("获取首页物品信息")
    public Result getHomeItem(@Valid @RequestBody ItemHomeDTO itemHomeDTO) {
        return ResultUtils.success(itemService.getHomeItem(itemHomeDTO));
    }

    /**
     * 修改物品信息
     */
    @PostMapping("/changeItemInfo")
    @ApiOperation("修改物品信息")
    public Result changeItemInfo(@RequestBody Item item) {
        itemService.updateItem(item);
        return ResultUtils.success();
    }

    /**
     * todo 前端没实现
     * 删除物品 需要删除redis的缓存 和 数据库的数据
     *
     * @param id
     * @param category
     * @return
     */
    @PostMapping("/deleteItem/{id}/{category}")
    public Result deleteItem(@PathVariable Long id, @PathVariable Boolean category) {
        itemService.deleteItem(id, category);
        return ResultUtils.success();
    }

    /**
     * 认领物品 向发布者推送信息 (未使用）
     *
     * @param wxInfoDTO
     * @return
     */
    @PostMapping("/claimItem")
    public Result claimItem(@RequestBody WxInfoDTO wxInfoDTO) {
        itemService.claimItem(wxInfoDTO);
        return ResultUtils.success();
    }
}
