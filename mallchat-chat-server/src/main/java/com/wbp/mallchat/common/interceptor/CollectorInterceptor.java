package com.wbp.mallchat.common.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.wbp.mallchat.common.domain.dto.RequestInfo;
import com.wbp.mallchat.common.exception.HttpErrorEnum;
import com.wbp.mallchat.common.user.service.LoginService;
import com.wbp.mallchat.common.utils.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class CollectorInterceptor implements HandlerInterceptor {                   

    @Autowired
    private LoginService loginServer;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestInfo info = new RequestInfo();
        info.setUid(Optional.ofNullable(request.getAttribute(TokenInterceptor.ATTRIBUTE_UID)).map(Object::toString).map(Long::parseLong).orElse(null));
        info.setIp(ServletUtil.getClientIP(request));
        RequestHolder.set(info);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
