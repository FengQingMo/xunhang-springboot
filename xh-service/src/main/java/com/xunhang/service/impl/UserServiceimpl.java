package com.xunhang.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bx.imclient.IMClient;
import com.bx.imcommon.enums.IMTerminalType;
import com.xunhang.common.enums.ExceptionEnums;
import com.xunhang.common.enums.ResultCode;
import com.xunhang.common.exception.GlobalException;
import com.xunhang.common.properties.JwtProperties;
import com.xunhang.common.properties.WeChatProperties;
import com.xunhang.common.result.Result;
import com.xunhang.common.result.ResultUtils;
import com.xunhang.common.utils.JwtUtil;
import com.xunhang.common.utils.UserUtil;
import com.xunhang.mapper.UserMapper;
import com.xunhang.pojo.dto.LoginDTO;
import com.xunhang.pojo.dto.ModifyPwdDTO;
import com.xunhang.pojo.dto.RegisterDTO;
import com.xunhang.pojo.entity.Friend;
import com.xunhang.pojo.entity.User;
import com.xunhang.pojo.vo.LoginVO;
import com.xunhang.pojo.vo.OnlineTerminalVO;
import com.xunhang.pojo.vo.UserLoginVO;
import com.xunhang.pojo.vo.UserVO;
import com.xunhang.service.IFriendService;
import com.xunhang.service.UserService;
import com.xunhang.session.IMSessionInfo;
import com.xunhang.utils.BeanUtils;
import com.xunhang.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static com.xunhang.common.constant.RedisConstants.*;
import static com.xunhang.common.enums.ResultCode.OLD_PASSWORD_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceimpl extends ServiceImpl<UserMapper, User> implements UserService {

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    //读写锁控制大厅物品缓存读写
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final String[] str = {"https://minio.fengqingmo.top/blog/aurora/config/ecd9631d396bb0e8754d399cef6377c8.jpg"};

    private final PasswordEncoder passwordEncoder;

    private final StringRedisTemplate stringRedisTemplate;

    private final JwtProperties jwtProperties;

    private final UserMapper userMapper;

    private final WeChatProperties weChatProperties;

    @Override
    public User getUserByOpenid(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        //验证 token
        if (!JwtUtil.checkSign(refreshToken, jwtProperties.getRefreshTokenSecret())) {
            throw new GlobalException(ExceptionEnums.REFRESH_TOKEN_OVERDUE);
        }
        String strJson = JwtUtil.getInfo(refreshToken);
        Long userId = JwtUtil.getUserId(refreshToken);
        String accessToken = JwtUtil.sign(userId, strJson, jwtProperties.getUserTtl(), jwtProperties.getUserSecretKey());
        String newRefreshToken = JwtUtil.sign(userId, strJson, jwtProperties.getRefreshTokenExpireIn(), jwtProperties.getRefreshTokenSecret());
        LoginVO vo = new LoginVO();
        vo.setAccessToken(accessToken);
        vo.setAccessTokenExpiresIn((int) jwtProperties.getUserTtl());
        vo.setRefreshToken(newRefreshToken);
        vo.setRefreshTokenExpiresIn(jwtProperties.getRefreshTokenExpireIn());
        return vo;
    }

    @Override
    public Result register(RegisterDTO dto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (null != user) {
            return ResultUtils.error(ResultCode.USERNAME_ALREADY_REGISTER);
        }
        user = new User();
        BeanUtil.copyProperties(dto, user);
        //todo 加密
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPassword(user.getPassword());
        Random random = new Random();
        int i = random.nextInt(str.length);
        user.setHeadImage(str[i]);
        user.setHeadImageThumb(str[i]);
        this.save(user);
        log.info("注册用户，用户id:{},用户名:{},昵称:{}", user.getId(), dto.getUsername(), dto.getNickname());
        return Result.success("注册成功");
    }

    @Override
    public Result modifyPassword(ModifyPwdDTO dto) {
        Long id = UserUtil.getCurrentId();
        User user = getById(id);
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return ResultUtils.success(OLD_PASSWORD_ERROR);
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        this.updateById(user);
        log.info("用户修改密码，用户id:{},用户名:{},昵称:{}", user.getId(), user.getUsername(), user.getNickname());
        return Result.success("修改密码成功");
    }

    @Override
    public UserLoginVO wxLogin(String openid) {
        User user = query().eq("openid", openid).one();
        if (null == user) {
            user = new User();
            user.setOpenid(openid);
        }
        IMSessionInfo session = new IMSessionInfo();
        session.setTerminal(0);
        session.setUserId(user.getId());
        String strJson = JSON.toJSONString(session);
        String accessToken = JwtUtil.sign(user.getId(), strJson, jwtProperties.getUserTtl(), jwtProperties.getUserSecretKey());
        String refreshToken = JwtUtil.sign(user.getId(), strJson, jwtProperties.getRefreshTokenExpireIn(), jwtProperties.getRefreshTokenSecret());
        LoginVO vo = new LoginVO();
        vo.setAccessToken(accessToken);
        vo.setAccessTokenExpiresIn((int) jwtProperties.getUserTtl());
        vo.setRefreshToken(refreshToken);
        vo.setRefreshTokenExpiresIn(jwtProperties.getRefreshTokenExpireIn());
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtil.copyProperties(user, userLoginVO);
        BeanUtil.copyProperties(vo, userLoginVO);
        return userLoginVO;
    }

    @Override
    public String getOpenid(String code) {
        //调用微信接口服务，获得当前用户的openid
        Map<String, String> map = new HashMap<String, String>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }

    @Override
    public Result login(LoginDTO dto) {
        String username = dto.getUsername();
        String loginTimesKey = LOGIN_TIMES_KEY + username;
        //0.检查用户是否被锁定
        String attempts = stringRedisTemplate.opsForValue().get(loginTimesKey);
        if (attempts != null && Integer.parseInt(attempts) >= MAX_ATTEMPTS) {
            return ResultUtils.error(408, "用户已被锁定，请" + LOGIN_LOCK_TTL + "分钟稍后再试或联系管理员解锁");
        }
        //1.获得用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (null == user) {
            return ResultUtils.error(ResultCode.USERNAME_NOT_EXIST);
        }
        //2.验证密码 若错误则增加一次尝试次数
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            incrementLoginAttempts(loginTimesKey);
            return ResultUtils.error(ResultCode.PASSWORD_ERROR);
        }
        //3.登陆成功，生成token并删除redis的登录次数key
        //3.1 封装用户信息
        IMSessionInfo imSessionInfo = new IMSessionInfo(user.getId(), dto.getTerminal());
        String accessToken = JwtUtil.sign(user.getId(), JSONUtil.toJsonStr(imSessionInfo), jwtProperties.getUserTtl(), jwtProperties.getUserSecretKey());
        String refreshToken = JwtUtil.sign(user.getId(), jwtProperties.getRefreshTokenExpireIn(), jwtProperties.getRefreshTokenSecret());
        LoginVO vo = new LoginVO();
        vo.setAccessToken(accessToken);
        vo.setAccessTokenExpiresIn((int) jwtProperties.getUserTtl());
        vo.setRefreshToken(refreshToken);
        vo.setRefreshTokenExpiresIn(jwtProperties.getRefreshTokenExpireIn());
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        vo.setUserVO(userVO);
        //3.2 删除key
        stringRedisTemplate.delete(loginTimesKey);
        return Result.success(vo);
    }

    private void incrementLoginAttempts(String key) {
        // 如果key不存在，则设置初始尝试次数为1，否则增加1
        stringRedisTemplate.opsForValue().increment(key);
        stringRedisTemplate.expire(key, LOGIN_TIMES_TTL, TimeUnit.MINUTES);
    }

    @Override
    public UserVO getSelf() {
        Long id = UserUtil.getCurrentId();
        User user = getById(id);
        if (user == null) {
            throw new GlobalException(ExceptionEnums.USER_NOT_EXIST);
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }


    /****************** im 相关 ****************/
    private final IMClient imClient;
    private final IFriendService friendService;
    @Override
    public User findUserByUserName(String username) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getUsername, username);
        return getOne(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(UserVO vo) {
        if (!UserUtil.getCurrentId().equals(vo.getId())) {
            throw new GlobalException(ExceptionEnums.UPDATE_NOT_PERMIT);
        }
        User user = this.getById(vo.getId());
        if (Objects.isNull(user)) {
            throw new GlobalException(ExceptionEnums.USER_NOT_EXIST);
        }
        // 更新好友昵称和头像
        if (!user.getNickname().equals(vo.getNickname()) || !user.getHeadImageThumb().equals(vo.getHeadImageThumb())) {
            QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Friend::getFriendId, UserUtil.getCurrentId());
            List<Friend> friends = friendService.list(queryWrapper);
            for (Friend friend : friends) {
                friend.setFriendNickname(vo.getNickname());
                friend.setFriendHeadImage(vo.getHeadImageThumb());
            }
            friendService.updateBatchById(friends);
        }
        //// 更新群聊中的头像
        //if (!user.getHeadImageThumb().equals(vo.getHeadImageThumb())) {
        //    List<GroupMember> members = groupMemberService.findByUserId(session.getUserId());
        //    for (GroupMember member : members) {
        //        member.setHeadImage(vo.getHeadImageThumb());
        //    }
        //    groupMemberService.updateBatchById(members);
        //}
        // 更新用户信息
        user.setNickname(vo.getNickname());
        user.setHeadImage(vo.getHeadImage());
        user.setHeadImageThumb(vo.getHeadImageThumb());
        this.updateById(user);
        log.info("用户信息更新，用户:{}}", user);
    }

    @Override
    public UserVO findUserById(Long id) {
        User user = this.getById(id);
        UserVO vo = BeanUtils.copyProperties(user, UserVO.class);
        vo.setOnline(imClient.isOnline(id));
        return vo;
    }

    @Override
    public List<UserVO> findUserByName(String name) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(User::getUsername, name).or().like(User::getNickname, name).last("limit 20");
        List<User> users = query().like("username", name).or().like("nickname", name).last("limit 20").list();
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        List<Long> onlineUserIds = imClient.getOnlineUser(userIds);
        return users.stream().map(u -> {
            UserVO vo = BeanUtils.copyProperties(u, UserVO.class);
            vo.setOnline(onlineUserIds.contains(u.getId()));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OnlineTerminalVO> getOnlineTerminals(String userIds) {
        List<Long> userIdList = Arrays.stream(userIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
        // 查询在线的终端
        Map<Long, List<IMTerminalType>> terminalMap = imClient.getOnlineTerminal(userIdList);
        // 组装vo
        List<OnlineTerminalVO> vos = new LinkedList<>();
        terminalMap.forEach((userId, types) -> {
            List<Integer> terminals = types.stream().map(IMTerminalType::code).collect(Collectors.toList());
            vos.add(new OnlineTerminalVO(userId, terminals));
        });
        return vos;
    }
}
