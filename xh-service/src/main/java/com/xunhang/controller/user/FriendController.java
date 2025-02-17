package com.xunhang.controller.user;

import com.xunhang.common.result.Result;
import com.xunhang.common.result.ResultUtils;
import com.xunhang.common.utils.UserUtil;
import com.xunhang.pojo.entity.Friend;
import com.xunhang.pojo.vo.FriendVO;
import com.xunhang.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "好友")
@RestController
@RequestMapping("/im/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/list")
    @ApiOperation(value = "好友列表", notes = "获取好友列表")
    public Result<List<FriendVO>> findFriends() {
        Long userId = UserUtil.getCurrentId();
        List<Friend> friends = friendService.findFriendByUserId(userId);
        List<FriendVO> vos = friends.stream().map(f -> {
            FriendVO vo = new FriendVO();
            vo.setId(f.getFriendId());
            vo.setHeadImage(f.getFriendHeadImage());
            vo.setNickname(f.getFriendNickname());
            return vo;
        }).collect(Collectors.toList());
        return ResultUtils.success(vos);
    }


    @PostMapping("/add")
    @ApiOperation(value = "添加好友", notes = "双方建立好友关系")
    public Result addFriend(@NotEmpty(message = "好友id不可为空") @RequestParam("friendId") Long friendId) {
        friendService.addFriend(friendId);
        return ResultUtils.success();
    }

    @GetMapping("/find/{friendId}")
    @ApiOperation(value = "查找好友信息", notes = "查找好友信息")
    public Result<FriendVO> findFriend(@NotEmpty(message = "好友id不可为空") @PathVariable("friendId") Long friendId) {
        return ResultUtils.success(friendService.findFriend(friendId));
    }


    @DeleteMapping("/delete/{friendId}")
    @ApiOperation(value = "删除好友", notes = "解除好友关系")
    public Result delFriend(@NotEmpty(message = "好友id不可为空") @PathVariable("friendId") Long friendId) {
        friendService.delFriend(friendId);
        return ResultUtils.success();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新好友信息", notes = "更新好友头像或昵称")
    public Result modifyFriend(@Valid @RequestBody FriendVO vo) {
        friendService.update(vo);
        return ResultUtils.success();
    }


}

