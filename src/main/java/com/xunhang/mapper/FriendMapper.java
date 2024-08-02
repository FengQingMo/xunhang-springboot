package com.xunhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunhang.pojo.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface FriendMapper extends BaseMapper<Friend> {



    @Update("update friend set last_content = #{last_content},last_content_time = #{time} where user_id = #{user_id} and friend_id = #{friend_id} ")
    void updateFriend(Long user_id, Long friend_id, String last_content, LocalDateTime time,Integer unreadNum);
}