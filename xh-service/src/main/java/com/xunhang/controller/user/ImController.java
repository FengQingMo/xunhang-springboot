package com.xunhang.controller.user;

import com.xunhang.common.result.Result;
import com.xunhang.common.result.ResultUtils;
import com.xunhang.pojo.vo.OnlineTerminalVO;
import com.xunhang.service.ImService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author 风清默
 * @date 2024/8/3 14:08
 * @description: IM通讯控制器层
 */
@RestController
public class ImController {

    @Autowired
    private  ImService imService;

    /**
     * 判断用户哪个终端在线
     *
     * @param userIds 以逗号分隔的用户id
     * @return 返回在线的用户id的终端集合
     */
    @GetMapping("/terminal/online")
    @ApiOperation(value = "判断用户哪个终端在线", notes = "返回在线的用户id的终端集合")
    public Result<List<OnlineTerminalVO>> getOnlineTerminal(@RequestParam("userIds") String userIds) {
        return ResultUtils.success(imService.getOnlineTerminals(userIds));
    }
}
