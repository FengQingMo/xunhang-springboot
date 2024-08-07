package com.xunhang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunhang.common.result.Result;
import com.xunhang.pojo.dto.LoginDTO;
import com.xunhang.pojo.dto.ModifyPwdDTO;
import com.xunhang.pojo.dto.RegisterDTO;
import com.xunhang.pojo.entity.User;
import com.xunhang.pojo.vo.LoginVO;
import com.xunhang.pojo.vo.OnlineTerminalVO;
import com.xunhang.pojo.vo.UserLoginVO;
import com.xunhang.pojo.vo.UserVO;

import java.util.List;

/**
 * @author 22477
 */

public interface UserService extends IService<User> {

    /**
     * 通过 openid 获取用户信息
     *
     * @param openid
     * @return
     */
    User getUserByOpenid(String openid);

    /**
     * 刷新token 无感登录
     *
     * @param refreshToken
     * @return
     */
    LoginVO refreshToken(String refreshToken);

    /**
     * 注册
     *
     * @param dto
     * @return
     */
    Result register(RegisterDTO dto);

    /**
     * 修改密码
     *
     * @param dto
     * @return
     */
    Result modifyPassword(ModifyPwdDTO dto);

    /**
     * @param openid
     * @return
     */
    UserLoginVO wxLogin(String openid);

    String getOpenid(String code);

    Result login(LoginDTO dto);

    /**
     * 获取 登录用户 信息
     *
     * @return 用户VO
     */
    UserVO getSelf();

    /***** im相关 ******/
    User findUserByUserName(String username);

    /**
     * 更新用户信息，好友昵称和群聊昵称等冗余信息也会更新
     *
     * @param vo 用户信息vo
     */
    void update(UserVO vo);

    /**
     * 根据用户昵id查询用户以及在线状态
     *
     * @param id 用户id
     * @return 用户信息
     */
    UserVO findUserById(Long id);

    /**
     * 根据用户昵称查询用户，最多返回20条数据
     *
     * @param name 用户名或昵称
     * @return 用户列表
     */
    List<UserVO> findUserByName(String name);

    /**
     * 获取用户在线的终端类型
     *
     * @param userIds 用户id，多个用‘,’分割
     * @return 在线用户终端
     */
    List<OnlineTerminalVO> getOnlineTerminals(String userIds);
}
