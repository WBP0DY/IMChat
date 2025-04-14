package com.wbp.mallchat.common.user.controller;


import com.wbp.mallchat.common.domain.dto.RequestInfo;
import com.wbp.mallchat.common.interceptor.TokenInterceptor;
import com.wbp.mallchat.common.user.domain.entity.User;
import com.wbp.mallchat.common.user.domain.vo.req.ModifyNameReq;
import com.wbp.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.wbp.mallchat.common.user.service.UserService;
import com.wbp.mallchat.common.domain.vo.resp.ApiResult;
import com.wbp.mallchat.common.utils.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wbp
 * @since 2025-02-24
 */
@RestController
@RequestMapping("/capi/user")
@Api(tags = "用户相关")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo(@RequestParam HttpServletRequest request) {
        RequestInfo requestInfo = RequestHolder.get();
        return ApiResult.success(userService.getUserInfo(requestInfo.getUid()));
    }

    @PutMapping("/changeName")
    @ApiOperation("修改用户名称")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq request) {
        userService.modifyName(RequestHolder.get().getUid(), request.getName());
        return ApiResult.success();
    }
}

