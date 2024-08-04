package com.xunhang.interceptor;

import com.xunhang.common.enums.ExceptionEnums;
import com.xunhang.common.exception.GlobalException;
import com.xunhang.common.properties.JwtProperties;
import com.xunhang.common.utils.JwtUtil;
import com.xunhang.common.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURI();
        String remoteIpAddress = request.getRemoteHost();
        log.info(String.format("检测到：ip地址(%s)正在请求接口(%s)", remoteIpAddress, requestUrl));
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }
        String token = request.getHeader("accessToken");
        //2、校验令牌
        if (!JwtUtil.checkSign(token, jwtProperties.getUserSecretKey())) {
            //log.error("token已失效，url:{}", request.getRequestURI());
            throw new GlobalException(ExceptionEnums.INVALID_TOKEN);
        }
        Long userId = JwtUtil.getUserId(token);
        if(userId != null){
            log.info("当前员工id：{}", userId);
            UserUtil.setCurrentId(userId);
        }
            //3、通过，放行
            return true;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserUtil.removeCurrentId();
    }
}
