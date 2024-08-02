package com.xunhang.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhang.pojo.entity.ItemImage;
import com.xunhang.mapper.ItemImageMapper;
import com.xunhang.service.ItemImageService;
@Service
public class ItemImageServiceImpl extends ServiceImpl<ItemImageMapper, ItemImage> implements ItemImageService{

}
