package com.xunhang.controller.user;


import cn.hutool.core.util.BooleanUtil;
import com.xunhang.common.properties.JwtProperties;
import com.xunhang.common.result.PageResult;
import com.xunhang.common.result.Result;
import com.xunhang.constant.JwtClaimsConstant;
import com.xunhang.pojo.dto.*;
import com.xunhang.pojo.entity.User;
import com.xunhang.pojo.vo.CommentVO;
import com.xunhang.pojo.vo.ItemVO;
import com.xunhang.pojo.vo.UserLoginVO;
import com.xunhang.pojo.vo.UserVO;
import com.xunhang.service.UserService;
import com.xunhang.utils.JwtUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户相关信息
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;


    @Autowired
    private JwtProperties jwtProperties;



    @GetMapping("/getOpenid")
    @ApiOperation("获取openid")
    public Result<String> getOpenid(@RequestParam String code){
        log.info("getOpenid:code:{}",code);


        return Result.success(userService.getOpenid(code));
    }

    @GetMapping("/getUserInfoById/{id}")
    @ApiOperation("通过id获取用户信息")
    public Result<UserVO> getUserInfoById(@PathVariable Long id){
        User user = userService.getById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);

        return Result.success(userVO);
    }

    @ApiOperation(value = "微信登录")
    @PostMapping("/login/{openid}")
    public Result<UserLoginVO> login(@PathVariable("openid") String openid) {
        //if(openid == null){
        //    return Result.error("openid cannot be null");
        //}
        //log.info("微信登录:{}",openid);
        User user = userService.wxLogin(openid);

        //为微信用户生成jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);

        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user,userLoginVO);
        userLoginVO.setToken(token);


        return Result.success(userLoginVO);
    }

    /*
    * 登录
    * 如果有该openid 则返回此用户
    * 如果没有 则新建一个用户
    * */
    @GetMapping("/getUserInfoByOpenid")
    public Result getUserInfo(@RequestParam String openid){
        User user = userService.getUser(openid);
        return  Result.success(user);
    }
     /*
     * 基础信息
     * */
    @PostMapping("/updateInfo")
    public Result updateInfo(@RequestBody User user){
        //#.info("更新信息");
        Boolean ok = userService.updateInfo(user);
        if(!ok){
            return Result.error("修改失败");
        }
        return Result.success("修改成功");

    }

    @GetMapping("/getBaseData")
    @ApiOperation("获取三个数据")
    public Result getBaseData(){
        //log.info("getBaseData");
        Map<String,Integer> map = new HashMap<>();
        map = userService.getBaseData();
        return Result.success(map);
    }

    @ApiOperation("获取当前用户的发布信息")
    @GetMapping("/getMyItem/{category}")
    public  Result getMyItem(@PathVariable Boolean category){
        //log.info("获取当前用户的发布信息:{}",category);

        List<ItemVO> items = userService.getMyItem(category);
        return Result.success(items);

    }

    @PostMapping("/publish")
    @ApiOperation(value = "发布失物")
    public Result publish(@RequestBody ItemPublishDTO itemPublishDTO) throws IOException {

        //log.info("发布失物:{}",itemPublishDTO);
        //先保存失物记录，获取生成的IDaa
         Boolean ok =  userService.publish(itemPublishDTO);
        if(!ok){
            return Result.error("发布失败");
        }
        return Result.success("发布成功");
    }

    @PostMapping("/getHomeItem")
    @ApiOperation("获取首页物品信息")
    public Result<PageResult> getHomeItem(@Valid @RequestBody ItemHomeDTO  itemHomeDTO){
        //log.info("getHomeItem:{}",itemHomeDTO);
        PageResult pageResult =  userService.getHomeItem(itemHomeDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/publishComment")
    @ApiOperation("发布评论")
    public Result publishComment(@RequestBody  CommentDTO commentDTO){
        userService.saveComment(commentDTO);
        return Result.success("成功");
    }

    @GetMapping("/getComments/{id}")
    @ApiOperation("获取评论")
    public Result getComments(@PathVariable("id") String id){
        //log.info("getComments:{}",id);
       List<CommentVO> commentVOS = userService.getComments(id);
       return Result.success(commentVOS);

    }

    @PostMapping("/replyComment")
    @ApiOperation("回复评论")
    public Result replyComment(@RequestBody ReplyDTO replyDTO){
        log.info("replyComment:{}", replyDTO);
        Boolean flag = userService.replyComment(replyDTO);
        if(BooleanUtil.isTrue(flag)){
            return Result.success("发布成功");
        }
        return Result.error("发布失败");
    }


    @PostMapping("/changeItemInfo")
    @ApiOperation("修改物品信息")
    public Result changeItemInfo(@RequestBody ItemChangeDTO itemChangeDTO){
            userService.changeItemInfo(itemChangeDTO);
            return Result.success();
    }


    @PostMapping("/deleteItem/{id}/{category}")
    public Result deleteItem(@PathVariable Long id,@PathVariable Boolean category){
        userService.deleteItem(id,category);
        return Result.success();
    }
}
