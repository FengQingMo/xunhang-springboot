package com.bx.implatform.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bx.imcommon.model.IMSessionInfo;
import com.bx.imcommon.util.JwtUtil;
import com.bx.implatform.config.JwtProperties;
import com.bx.implatform.enums.ResultCode;
import com.bx.implatform.exception.GlobalException;
import com.bx.implatform.session.UserHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@AllArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //从 http 请求头中取出 token
        String token = request.getHeader("accessToken");
        if (StrUtil.isEmpty(token)) {
            log.error("未登陆，url:{}", request.getPathInfo());
            throw new GlobalException(ResultCode.NO_LOGIN);
        }
        //验证 token
        if (!JwtUtil.checkSign(token, jwtProperties.getAccessTokenSecret())) {
            log.error("token已失效，url:{}", request.getRequestURI());
            throw new GlobalException(ResultCode.INVALID_TOKEN);
        }
        // 存放session
        Long userId = JwtUtil.getUserId(token);
        // 存放session
        String strJson = JwtUtil.getInfo(token);
        IMSessionInfo userSession = JSON.parseObject(strJson, IMSessionInfo.class);
        request.setAttribute("session", userSession);
        UserHolder.setCurrentId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeCurrentId();
    }
}
