package com.xunhang.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bx.imclient.IMClient;
import com.bx.imcommon.contant.IMConstant;
import com.bx.imcommon.enums.IMTerminalType;
import com.bx.imcommon.model.IMPrivateMessage;
import com.bx.imcommon.model.IMUserInfo;
import com.xunhang.common.enums.ExceptionEnums;
import com.xunhang.common.enums.MessageStatus;
import com.xunhang.common.enums.MessageType;
import com.xunhang.common.exception.GlobalException;
import com.xunhang.common.utils.UserUtil;
import com.xunhang.mapper.PrivateMessageMapper;
import com.xunhang.pojo.dto.PrivateMessageDTO;
import com.xunhang.pojo.entity.Friend;
import com.xunhang.pojo.entity.PrivateMessage;
import com.xunhang.pojo.vo.PrivateMessageVO;
import com.xunhang.service.FriendService;
import com.xunhang.service.PrivateMessageService;
import com.xunhang.utils.BeanUtils;
import com.xunhang.utils.SensitiveFilterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessage> implements PrivateMessageService {

    private final FriendService friendService;

    private final IMClient imClient;

    private final SensitiveFilterUtil sensitiveFilterUtil;

    @Override
    public Long sendMessage(PrivateMessageDTO dto) {
        Boolean isFriends = friendService.isFriend(UserUtil.getCurrentId(), dto.getRecvId());
        if (Boolean.FALSE.equals(isFriends)) {
            throw new GlobalException(ExceptionEnums.NOT_YOUR_FRIEND);
        }
        // 保存消息
        PrivateMessage msg = BeanUtils.copyProperties(dto, PrivateMessage.class);
        msg.setSendId(UserUtil.getCurrentId());
        msg.setStatus(MessageStatus.UNSEND.code());
        msg.setSendTime(new Date());
        this.save(msg);
        // 过滤消息内容
        String content = sensitiveFilterUtil.filter(dto.getContent());
        msg.setContent(content);
        // 推送消息
        PrivateMessageVO msgInfo = BeanUtils.copyProperties(msg, PrivateMessageVO.class);
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        sendMessage.setSender(new IMUserInfo(UserUtil.getCurrentId(), 1));
        sendMessage.setRecvId(msgInfo.getRecvId());
        sendMessage.setSendToSelf(true);
        sendMessage.setData(msgInfo);
        sendMessage.setSendResult(true);
        imClient.sendPrivateMessage(sendMessage);
        log.info("发送私聊消息，发送id:{},接收id:{}，内容:{}", UserUtil.getCurrentId(), dto.getRecvId(), dto.getContent());
        return msg.getId();
    }

    @Override
    public void recallMessage(Long id) {
        PrivateMessage msg = this.getById(id);
        if (Objects.isNull(msg)) {
            throw new GlobalException(ExceptionEnums.MESSAGE_NOT_EXIST);
        }
        if (!msg.getSendId().equals(UserUtil.getCurrentId())) {
            throw new GlobalException(ExceptionEnums.NOT_YOUR_MESSAGE);
        }
        if (System.currentTimeMillis() - msg.getSendTime().getTime() > IMConstant.ALLOW_RECALL_SECOND * 1000) {
            throw new GlobalException(ExceptionEnums.MESSAGE_TIME_OUT);
        }
        // 修改消息状态
        msg.setStatus(MessageStatus.RECALL.code());
        this.updateById(msg);
        // 推送消息
        PrivateMessageVO msgInfo = BeanUtils.copyProperties(msg, PrivateMessageVO.class);
        msgInfo.setType(MessageType.RECALL.code());
        msgInfo.setSendTime(new Date());
        msgInfo.setContent("对方撤回了一条消息");
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        // terminal
        sendMessage.setSender(new IMUserInfo(UserUtil.getCurrentId(), 1));
        sendMessage.setRecvId(msgInfo.getRecvId());
        sendMessage.setSendToSelf(false);
        sendMessage.setData(msgInfo);
        sendMessage.setSendResult(false);
        imClient.sendPrivateMessage(sendMessage);
        // 推给自己其他终端
        msgInfo.setContent("你撤回了一条消息");
        sendMessage.setSendToSelf(true);
        sendMessage.setRecvTerminals(Collections.emptyList());
        imClient.sendPrivateMessage(sendMessage);
        log.info("撤回私聊消息，发送id:{},接收id:{}，内容:{}", msg.getSendId(), msg.getRecvId(), msg.getContent());
    }

    @Override
    public List<PrivateMessageVO> findHistoryMessage(Long friendId, Long page, Long size) {
        page = page > 0 ? page : 1;
        size = size > 0 ? size : 10;
        Long userId = UserUtil.getCurrentId();
        long stIdx = (page - 1) * size;
        QueryWrapper<PrivateMessage> wrapper = new QueryWrapper<>();
        wrapper.lambda().and(wrap -> wrap.and(
                                wp -> wp.eq(PrivateMessage::getSendId, userId)
                                        .eq(PrivateMessage::getRecvId, friendId))
                        .or(wp -> wp.eq(PrivateMessage::getRecvId, userId)
                                .eq(PrivateMessage::getSendId, friendId)))
                .ne(PrivateMessage::getStatus, MessageStatus.RECALL.code())
                .orderByDesc(PrivateMessage::getId)
                .last("limit " + stIdx + "," + size);
        List<PrivateMessage> messages = this.list(wrapper);
        List<PrivateMessageVO> messageInfos = messages.stream().map(m -> BeanUtils.copyProperties(m, PrivateMessageVO.class)).collect(Collectors.toList());
        log.info("拉取聊天记录，用户id:{},好友id:{}，数量:{}", userId, friendId, messageInfos.size());
        return messageInfos;
    }

    @Override
    public List<PrivateMessageVO> loadMessage(Long minId) {
        List<Friend> friends = friendService.findFriendByUserId(UserUtil.getCurrentId());
        if (friends.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> friendIds = friends.stream().map(Friend::getFriendId).collect(Collectors.toList());
        // 获取当前用户的消息
        LambdaQueryWrapper<PrivateMessage> queryWrapper = Wrappers.lambdaQuery();
        // 只能拉取最近1个月的
        Date minDate = DateUtils.addMonths(new Date(), -1);
        queryWrapper.gt(PrivateMessage::getId, minId)
                .ge(PrivateMessage::getSendTime, minDate)
                .ne(PrivateMessage::getStatus, MessageStatus.RECALL.code())
                .and(wrap -> wrap.and(
                                wp -> wp.eq(PrivateMessage::getSendId, UserUtil.getCurrentId())
                                        .in(PrivateMessage::getRecvId, friendIds))
                        .or(wp -> wp.eq(PrivateMessage::getRecvId, UserUtil.getCurrentId())
                                .in(PrivateMessage::getSendId, friendIds)))
                .orderByAsc(PrivateMessage::getId)
                .last("limit 100");
        List<PrivateMessage> messages = this.list(queryWrapper);
        // 更新发送状态
        List<Long> ids = messages.stream()
                .filter(m -> !m.getSendId().equals(UserUtil.getCurrentId()) && m.getStatus().equals(MessageStatus.UNSEND.code()))
                .map(PrivateMessage::getId)
                .collect(Collectors.toList());
        if (!ids.isEmpty()) {
            LambdaUpdateWrapper<PrivateMessage> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.in(PrivateMessage::getId, ids)
                    .set(PrivateMessage::getStatus, MessageStatus.SENDED.code());
            this.update(updateWrapper);
        }
        log.info("拉取消息，用户id:{},数量:{}", UserUtil.getCurrentId(), messages.size());
        return messages.stream().map(m -> BeanUtils.copyProperties(m, PrivateMessageVO.class)).collect(Collectors.toList());
    }

    @Override
    public void pullOfflineMessage(Long minId) {
        if (!imClient.isOnline(UserUtil.getCurrentId())) {
            throw new GlobalException(ExceptionEnums.NETWORK_ERROR);
        }
        // 查询用户好友列表
        List<Friend> friends = friendService.findFriendByUserId(UserUtil.getCurrentId());
        if (friends.isEmpty()) {
            // 关闭加载中标志
            this.sendLoadingMessage(false);
            return;
        }
        // 开启加载中标志
        this.sendLoadingMessage(true);
        List<Long> friendIds = friends.stream().map(Friend::getFriendId).collect(Collectors.toList());
        // 获取当前用户的消息
        LambdaQueryWrapper<PrivateMessage> queryWrapper = Wrappers.lambdaQuery();
        // 只能拉取最近1个月的1000条消息
        Date minDate = DateUtils.addMonths(new Date(), -1);
        List<PrivateMessage> messages = query().gt("id", minId)
                .ge("send_time", minDate)
                .ne("status", MessageStatus.RECALL.code())
                .and(wrap -> wrap.and(wp -> wp.eq("send_id", UserUtil.getCurrentId())
                                .in("recv_id", friendIds))
                        .or(wp -> wp.eq("recv_id", UserUtil.getCurrentId())
                                .in("send_id", friendIds)))
                .orderByDesc("id")
                .last("limit 1000").list();
        // 消息顺序从小到大
        CollectionUtil.reverse(messages);
        // 推送消息
        for (PrivateMessage m : messages) {
            PrivateMessageVO vo = BeanUtils.copyProperties(m, PrivateMessageVO.class);
            IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
            sendMessage.setSender(new IMUserInfo(m.getSendId(), IMTerminalType.WEB.code()));
            sendMessage.setRecvId(UserUtil.getCurrentId());
            //todo
            sendMessage.setRecvTerminals(Arrays.asList(1));
            sendMessage.setSendToSelf(false);
            sendMessage.setData(vo);
            sendMessage.setSendResult(true);
            imClient.sendPrivateMessage(sendMessage);
        }
        // 关闭加载中标志
        this.sendLoadingMessage(false);
        log.info("拉取私聊消息，用户id:{},数量:{}", UserUtil.getCurrentId(), messages.size());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void readedMessage(Long friendId) {
        // 推送消息给自己，清空会话列表上的已读数量
        PrivateMessageVO msgInfo = new PrivateMessageVO();
        msgInfo.setType(MessageType.READED.code());
        msgInfo.setSendId(UserUtil.getCurrentId());
        msgInfo.setRecvId(friendId);
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        sendMessage.setData(msgInfo);
        // todo
        sendMessage.setSender(new IMUserInfo(UserUtil.getCurrentId(), 1));
        sendMessage.setSendToSelf(true);
        sendMessage.setSendResult(false);
        imClient.sendPrivateMessage(sendMessage);
        // 推送回执消息给对方，更新已读状态
        msgInfo = new PrivateMessageVO();
        msgInfo.setType(MessageType.RECEIPT.code());
        msgInfo.setSendId(UserUtil.getCurrentId());
        msgInfo.setRecvId(friendId);
        sendMessage = new IMPrivateMessage<>();
        //todo
        sendMessage.setSender(new IMUserInfo(UserUtil.getCurrentId(), 1));
        sendMessage.setRecvId(friendId);
        sendMessage.setSendToSelf(false);
        sendMessage.setSendResult(false);
        sendMessage.setData(msgInfo);
        imClient.sendPrivateMessage(sendMessage);
        // 修改消息状态为已读
        LambdaUpdateWrapper<PrivateMessage> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(PrivateMessage::getSendId, friendId)
                .eq(PrivateMessage::getRecvId, UserUtil.getCurrentId())
                .eq(PrivateMessage::getStatus, MessageStatus.SENDED.code())
                .set(PrivateMessage::getStatus, MessageStatus.READED.code());
        this.update(updateWrapper);
        log.info("消息已读，接收方id:{},发送方id:{}", UserUtil.getCurrentId(), friendId);
    }

    @Override
    public Long getMaxReadedId(Long friendId) {
        LambdaQueryWrapper<PrivateMessage> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PrivateMessage::getSendId, UserUtil.getCurrentId())
                .eq(PrivateMessage::getRecvId, friendId)
                .eq(PrivateMessage::getStatus, MessageStatus.READED.code())
                .orderByDesc(PrivateMessage::getId)
                .select(PrivateMessage::getId)
                .last("limit 1");
        PrivateMessage message = this.getOne(wrapper);
        if (Objects.isNull(message)) {
            return -1L;
        }
        return message.getId();
    }

    private void sendLoadingMessage(Boolean isLoadding) {
        PrivateMessageVO msgInfo = new PrivateMessageVO();
        msgInfo.setType(MessageType.LOADDING.code());
        msgInfo.setContent(isLoadding.toString());
        IMPrivateMessage sendMessage = new IMPrivateMessage<>();
        //todo
        sendMessage.setSender(new IMUserInfo(UserUtil.getCurrentId(), 1));
        sendMessage.setRecvId(UserUtil.getCurrentId());
        sendMessage.setRecvTerminals(Arrays.asList(1));
        sendMessage.setData(msgInfo);
        sendMessage.setSendToSelf(false);
        sendMessage.setSendResult(false);
        imClient.sendPrivateMessage(sendMessage);
    }
}
