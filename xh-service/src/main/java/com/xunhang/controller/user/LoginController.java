package com.xunhang.controller.user;

import com.xunhang.common.enums.ExceptionEnums;
import com.xunhang.common.exception.GlobalException;
import com.xunhang.common.result.Result;
import com.xunhang.common.result.ResultUtils;
import com.xunhang.pojo.dto.LoginDTO;
import com.xunhang.pojo.dto.RegisterDTO;
import com.xunhang.pojo.vo.LoginVO;
import com.xunhang.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 登录、注册、登出等功能
 */
@Api(tags = "用户登录和注册")
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 账号密码登录模式
     *
     * @param dto 账号密码
     * @return 用户信息及jwt
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public Result login(@Valid @RequestBody LoginDTO dto) {
        return userService.login(dto);
    }

    /**
     * 获取用户在微信的唯一标识 openid
     *
     * @param code 前端小程序登录获取的授权码
     */
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

    /**
     * 通过 openid 登录
     */
    @ApiOperation(value = "微信登录")
    @PostMapping("/login/{openid}")
    public Result login(@PathVariable("openid") String openid) {
        return ResultUtils.success(userService.wxLogin(openid));
    }

    /**
     * 刷新token 实现无感登录
     */
    @PutMapping("/refreshToken")
    @ApiOperation(value = "刷新token", notes = "用refreshtoken换取新的token")
    public Result refreshToken(@RequestHeader("refreshToken") String refreshToken) {
        LoginVO vo = userService.refreshToken(refreshToken);
        return ResultUtils.success(vo);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public Result register(@Valid @RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }
}
