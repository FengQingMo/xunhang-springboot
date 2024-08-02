package com.xunhang.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhang.common.UserHolder;
import com.xunhang.common.properties.WeChatProperties;
import com.xunhang.common.result.PageResult;
import com.xunhang.mapper.UserMapper;
import com.xunhang.pojo.dto.*;
import com.xunhang.pojo.entity.*;
import com.xunhang.pojo.vo.CommentVO;
import com.xunhang.pojo.vo.ItemVO;
import com.xunhang.pojo.vo.ReplyVO;
import com.xunhang.pojo.vo.UserVO;
import com.xunhang.service.*;
import com.xunhang.utils.AliOssUtil;
import com.xunhang.utils.HttpClientUtil;
import com.xunhang.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static com.xunhang.constant.RedisConstants.IDS_ZSET_KEY;
import static com.xunhang.constant.RedisConstants.ITEM_KEY;

@Service
@Slf4j
public class UserServiceimpl  extends ServiceImpl<UserMapper, User> implements UserService {



    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AliOssUtil aliOSSUtils;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReplyService replyService;


    @Autowired
    private ItemImageService itemImageService;


    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);
    //读写锁控制大厅物品缓存读写
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    private final   String[] str = {"https://xunhang.oss-cn-beijing.aliyuncs.com/avatar/default_avatar/avatar1.jpg","https://xunhang.oss-cn-beijing.aliyuncs.com/avatar/default_avatar/avatar2.jpg","https://xunhang.oss-cn-beijing.aliyuncs.com/avatar/default_avatar/avatar3.jpg"};

    @Autowired
    private WeChatProperties weChatProperties;



    @Override
    public User wxLogin(String openid) {


        User user = query().eq("openid", openid).one();
        if(user == null){
            User user1 = new User();
            Random random = new Random();
            int index = random.nextInt(3);
            user1.setAvatarUrl(str[index]);
            user1.setOpenid(openid);
            user1.setNickname("寻航用户" + user1.getId());
            save(user1);
            return  user1;
        }

        return user;
    }

    @Override
    public String getOpenid(String code) {
        //调用微信接口服务，获得当前用户的openid
        Map<String,String> map = new HashMap<String,String>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type","authorization_code");
        String response = HttpClientUtil.doGet(WX_LOGIN, map);
        log.info(response);
        if(response!=null){
            return JSON.parseObject(response).getString("openid");
        }
       return "微信调用接口失败";
    }

    @Override
    public User getUser(String openid) {
        return userMapper.getUser(openid);
    }

    @Override
    public Boolean updateInfo(User user) {
        Long id = UserHolder.getCurrentId();
        user.setUpdateTime(LocalDateTime.now());
        log.info("user:{}",user);
        boolean ok = update().set("nickname", user.getNickname()).set("avatar_url", user.getAvatarUrl()).eq("id", id).update();
        return BooleanUtil.isTrue(ok);
    }

    @Override
    public Map<String,Integer>getBaseData(){
        Long id = UserHolder.getCurrentId();
        int found = 0,lost = 0, unclaimed =0;
        List<Item> list = itemService.query().eq("publisher_id", id).list();

        for (Item item : list) {
            //log.info("item:{}",item);
            if(item.getCategory() == null || item.getIsSuccess() == null )
                continue;
            if(!item.getCategory()){
                if(!item.getIsSuccess()){
                    lost +=1;
                }
                else{
                    found +=1;
                }
            }
            else {
                if(!item.getIsSuccess()){
                    unclaimed +=1;
                }
            }
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("found",found);
        map.put("lost",lost);
        map.put("unclaimed",unclaimed);
        return map;
    }

    @Override
    public  List<ItemVO> getMyItem(Boolean category) {
        Long id = UserHolder.getCurrentId();


        List<Item> list = itemService.query().eq("publisher_id", id).eq("category", category).list();
        List<ItemVO>itemVOS = new ArrayList<ItemVO>();

        for (Item item : list) {
            ItemVO vo = new ItemVO();
            BeanUtil.copyProperties(item,vo);
           List<ItemImage>itemImages = itemImageService.query().eq("item_id",item.getId()).list();

           List<String>images = new ArrayList<String>();

           itemImages.stream().forEach((itemImage ->images.add(itemImage.getImageUrl()) ));
           vo.setImages(images);
            itemVOS.add(vo);
        }
        return itemVOS;
    }

    @Override
    public Boolean publish(ItemPublishDTO itemPublishDTO){
        Long id = UserHolder.getCurrentId();

        //保存物品记录
        Item item = new Item();
        BeanUtils.copyProperties(itemPublishDTO,item);
        item.setPublisherId(id);
        item.setClaimerId(0L);
        item.setIsSuccess(false);
        item.setDate(LocalDate.now());
        itemService.save(item);

        //保存图片
        Boolean ok = true;
        List<String> images = itemPublishDTO.getImages();
        if(images != null && !images.isEmpty()){
            List<ItemImage>itemImages = new ArrayList<>();

            for (String image : images) {
                ItemImage itemImage = new ItemImage();
                itemImage.setImageUrl(image);
                itemImage.setItemId(item.getId());
                itemImages.add(itemImage);
            }

             ok = itemImageService.saveBatch(itemImages);
        }

        return BooleanUtil.isTrue(ok);

    }


    @Override
    public PageResult getHomeItem(ItemHomeDTO itemHomeDTO){
        String text = itemHomeDTO.getText();
        Boolean category = itemHomeDTO.getCategory();
        String tag = itemHomeDTO.getTag();


        List<ItemVO> itemVOS = new ArrayList<>();
        Page<Item>page = new Page<>();

        PageResult pageResult = new PageResult();
     if(tag ==null && text ==null){
         List<ItemVO> itemByRedis = getItemByRedis(category, itemHomeDTO.getPage(), itemHomeDTO.getLimit());
         if(!itemByRedis.isEmpty()) {
             itemVOS = itemByRedis;
         }else{
             return getHomeItemByDB(itemHomeDTO);

         }

     }else{
         return getHomeItemByDB(itemHomeDTO);
     }


        Integer count =itemService.query().eq("category", category).count();
        pageResult.setRecords(itemVOS);
        pageResult.setTotal(count);

        return pageResult;
    }
    private PageResult getHomeItemByDB(ItemHomeDTO itemHomeDTO) {
        String text = itemHomeDTO.getText();
        Boolean category = itemHomeDTO.getCategory();
        String tag = itemHomeDTO.getTag();


        List<ItemVO> itemVOS = new ArrayList<>();
        Page<Item> page = new Page<>();
        page = itemService.query().eq(itemHomeDTO.getTag()!=null, "tag", tag)
                .eq("category", category)
                .like(itemHomeDTO.getText()!=null, "title", text)
                .page(new Page<>(itemHomeDTO.getPage(), itemHomeDTO.getLimit()));
        List<Item> items = page.getRecords();

        for (Item item : items) {
            log.info("item:{}", item);
            ItemVO itemVO = new ItemVO();
            BeanUtil.copyProperties(item, itemVO);

            //获取物品对应图片
            List<String> images = itemImageService.listObjs(
                    Wrappers.<ItemImage>lambdaQuery()
                            .eq(ItemImage::getItemId, item.getId())
                            .select(ItemImage::getImageUrl),
                    obj -> (String) obj // 映射成 String 类型
            );

            User user = getById(item.getPublisherId());
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user, userVO);

            itemVO.setUserVO(userVO);
            itemVO.setImages(images);
            itemVOS.add(itemVO);
        }

        rebuilRedisCacheAsync(itemVOS,category);
        PageResult pageResult = new PageResult();
        pageResult.setRecords(itemVOS);
        pageResult.setTotal(page.getTotal());
        return pageResult;
    }

    @Async
    protected void rebuilRedisCacheAsync(List<ItemVO> itemVOS,Boolean category){
        boolean lock = readWriteLock.writeLock().tryLock();
        if(lock){
            try {
                itemVOS.forEach(itemVO -> {
                    Long id = itemVO.getId();
                    stringRedisTemplate.opsForZSet().add(IDS_ZSET_KEY+category,String.valueOf(id),id);
                    String jsonStr = JSONUtil.toJsonStr(itemVO);
                    stringRedisTemplate.opsForValue().set(ITEM_KEY+id,JSONUtil.toJsonStr(itemVO));
                });

            }finally {
                readWriteLock.writeLock().unlock();
            }
        }

    }
    private List<ItemVO> getItemByRedis(Boolean category, Integer index, Integer count) {
        // 构造Redis缓存键
        String cacheKey = IDS_ZSET_KEY + category;
        int start = (index - 1) * count;
        int end = start + count - 1;

        // 尝试从Redis获取数据
        List<ItemVO>itemList = new ArrayList<ItemVO>();
        readWriteLock.readLock().lock();
      try {
          List<Integer> ids = Objects.requireNonNull(stringRedisTemplate.opsForZSet().range(cacheKey, start, end)).stream().map(Integer::valueOf).collect(Collectors.toList());

          List<String> idList = ids.stream().map(id -> ITEM_KEY +id).collect(Collectors.toList());

          List<String>items =stringRedisTemplate.opsForValue().multiGet(idList);
         itemList = new ArrayList<>();
          if (items != null) {
              itemList =  items.stream().map(item -> JSONUtil.toBean(item,ItemVO.class)).collect(Collectors.toList());
          }
      }finally {
          readWriteLock.readLock().unlock();
      }

        return itemList;
    }



    @Override
    public void saveComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtil.copyProperties(commentDTO,comment);
        comment.setPublisherId(UserHolder.getCurrentId());
        comment.setTime(LocalDateTime.now());

        commentService.save(comment);
    }

    @Override
    public List<CommentVO> getComments(String id) {
        List<Comment> comments = commentService.query().eq("item_id", id).list();

        List<CommentVO> commentVOS = new ArrayList<CommentVO>();
        comments.forEach(comment -> {
            User user = getById(comment.getPublisherId());
            UserVO userVO = new UserVO();
            if(user != null)
                 BeanUtil.copyProperties(user,userVO);
            CommentVO commentVO = new CommentVO();
            BeanUtil.copyProperties(comment,commentVO);
            commentVO.setUserVO(userVO);

            List<ReplyVO> replyVOList = getReplyCommentVOList(comment.getId());
            commentVO.setReplyVOList(replyVOList);
            commentVOS.add(commentVO);
        });
        return commentVOS;
    }

    @Override
    public Boolean replyComment(ReplyDTO replyDTO) {
        Reply reply = new Reply();
        BeanUtil.copyProperties(replyDTO,reply);
        reply.setTime(LocalDateTime.now());
        reply.setPublisherId(UserHolder.getCurrentId());
        boolean save = replyService.save(reply);
        if(save) return true;
        else return false;
    }

    @Override
    public void changeItemInfo(ItemChangeDTO itemChangeDTO) {
        String itemKey = ITEM_KEY + itemChangeDTO.getId();

        Item item = new Item();
        BeanUtil.copyProperties(itemChangeDTO,item);
        itemService.saveOrUpdate(item);

       try {
           Boolean lock = redisUtil.tryLock(itemKey);
           if(lock){
               stringRedisTemplate.opsForValue().set(itemKey,JSONUtil.toJsonStr(itemChangeDTO));
           }
       }finally {
           redisUtil.unlock(itemKey);
       }

    }

    //获取评论下的回复
    public List<ReplyVO> getReplyCommentVOList(Long commentId) {
        List<Reply> replyList = replyService.query().eq("comment_id", commentId).list();

        List<ReplyVO> replyVOList = new ArrayList<ReplyVO>();
        replyList.forEach(reply ->{
            User user = getById(reply.getPublisherId());
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user,userVO);

            ReplyVO replyVO = new ReplyVO();
            BeanUtil.copyProperties(reply, replyVO);

            replyVO.setUserVO(userVO);
            replyVOList.add(replyVO);

        });

        return replyVOList;
    }


    public void deleteItem(Long id,Boolean category){
            stringRedisTemplate.opsForZSet().remove(IDS_ZSET_KEY+category,String.valueOf(id));
    }

}
