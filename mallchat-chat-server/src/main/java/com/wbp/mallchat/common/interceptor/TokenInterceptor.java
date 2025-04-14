package com.wbp.mallchat.common.interceptor;

import com.wbp.mallchat.common.exception.HttpErrorEnum;
import com.wbp.mallchat.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final String HEADER_AUTHORIZATION = "authorization";
    public static final String AUTHORIZATION_SCHEMA = "Bearer ";
    public static final String ATTRIBUTE_UID = "uId";

    @Autowired
    private LoginService loginServer;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        Long validUid = loginServer.getValidUid(token);
        if (Objects.nonNull(validUid)) {
            // 用户已登录
            request.setAttribute(ATTRIBUTE_UID, validUid);
        } else {
            // 用户未登录
            boolean isPublic = isPublic(request);
            if (!isPublic) {
                // 非公共方法
                HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
                return false;
            }
        }
        return true;
    }

    private static boolean isPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        return Stream.of(split).anyMatch(element -> "public".equals(element));
    }

    private String getToken(HttpServletRequest request) {
        // 获取协议+token
        String header = request.getHeader(HEADER_AUTHORIZATION);
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(AUTHORIZATION_SCHEMA))
                .map(h -> h.replaceFirst(AUTHORIZATION_SCHEMA, ""))
                .orElse(null);
    }
}
