package com.xunhang.controller.user;

import com.xunhang.common.enums.ExceptionEnums;
import com.xunhang.common.exception.GlobalException;
import com.xunhang.common.result.Result;
import com.xunhang.common.result.ResultUtils;
import com.xunhang.pojo.dto.ModifyPwdDTO;
import com.xunhang.pojo.entity.User;
import com.xunhang.pojo.vo.OnlineTerminalVO;
import com.xunhang.pojo.vo.UserVO;
import com.xunhang.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取自己信息
     */
    @GetMapping("/self")
    @ApiOperation(("获取自己信息"))
    public Result<UserVO> getSelf() {
        return ResultUtils.success(userService.getSelf());
    }

    /**
     * 修改用户密码
     */
    @PutMapping("/modifyPwd")
    @ApiOperation(value = "修改密码", notes = "修改用户密码")
    public Result modifyPassword(@Valid @RequestBody ModifyPwdDTO dto) {
        return userService.modifyPassword(dto);
    }

    /**
     * 通过 openid 获取用户信息
     */
    @GetMapping("/getUserInfoByOpenid")
    public Result getUserInfoByOpenid(@RequestParam String openid) {
        return ResultUtils.success(userService.getUserByOpenid(openid));
    }

    /**
     * 修改个人信息
     *
     * @param user
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改用户信息")
    public Result update(@RequestBody User user) {
        return ResultUtils.success(userService.updateById(user));
    }

    @GetMapping("/getOpenid")
    @ApiOperation("获取openid")
    public Result<String> getOpenid(@RequestParam String code) {
        String openid;
        try {
            openid = userService.getOpenid(code);
        } catch (Exception e) {
            throw new GlobalException(ExceptionEnums.GET_OPENID_ERROR);
        }
        return ResultUtils.success(openid);
    }
    /********************* im相关 **********************/
    @GetMapping("/terminal/online")
    @ApiOperation(value = "判断用户哪个终端在线", notes = "返回在线的用户id的终端集合")
    public Result<List<OnlineTerminalVO>> getOnlineTerminal(@NotEmpty @RequestParam("userIds") String userIds) {
        return ResultUtils.success(userService.getOnlineTerminals(userIds));
    }


    @GetMapping("/find/{id}")
    @ApiOperation(value = "查找用户", notes = "根据id查找用户")
    public Result<UserVO> findById(@NotEmpty @PathVariable("id") Long id) {
        return ResultUtils.success(userService.findUserById(id));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息，仅允许修改登录用户信息")
    public Result update(@Valid @RequestBody UserVO vo) {
        userService.update(vo);
        return ResultUtils.success();
    }

    @GetMapping("/findByName")
    @ApiOperation(value = "查找用户", notes = "根据用户名或昵称查找用户")
    public Result<List<UserVO>> findByName(@RequestParam("name") String name) {
        return ResultUtils.success(userService.findUserByName(name));
    }
}
