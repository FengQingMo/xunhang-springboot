package com.xunhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunhang.pojo.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    /**
     * 查询前三条公告
     */
    @Select("SELECT content FROM announcement ORDER BY create_time DESC LIMIT 3")
    List<String> selectTop3Contents();
}
