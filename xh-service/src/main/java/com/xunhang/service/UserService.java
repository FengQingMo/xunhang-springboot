package com.xunhang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunhang.common.result.Result;
import com.xunhang.pojo.dto.LoginDTO;
import com.xunhang.pojo.dto.ModifyPwdDTO;
import com.xunhang.pojo.dto.RegisterDTO;
import com.xunhang.pojo.entity.User;
import com.xunhang.pojo.vo.LoginVO;
import com.xunhang.pojo.vo.UserLoginVO;
import com.xunhang.pojo.vo.UserVO;

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
}
