package com.xunhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunhang.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {


    User getUser(String openid);

    void insertUser(User newUser);

    void updateInfo( User user);


}
