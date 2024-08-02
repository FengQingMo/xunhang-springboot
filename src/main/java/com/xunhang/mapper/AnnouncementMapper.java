package com.xunhang.mapper;


import com.xunhang.pojo.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnnouncementMapper {
    @Select("select * from announcement")
    List<Announcement> getContent();
}
