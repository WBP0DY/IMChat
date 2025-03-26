package com.wbp.mallchat.common.websocket.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.wbp.mallchat.common.user.dao.UserDao;
import com.wbp.mallchat.common.user.domain.entity.User;
import com.wbp.mallchat.common.user.service.UserService;
import com.wbp.mallchat.common.user.service.adapter.TextBuilder;
import com.wbp.mallchat.common.websocket.service.WXMsgService;
import com.wbp.mallchat.common.websocket.service.WebSocketService;
import com.wbp.mallchat.common.websocket.service.adapter.UserAdapter;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class WXMsgServiceImpl implements WXMsgService {

    @Autowired
    private WebSocketService webSocketService;

    /**
     * openid和登录code的关系map  可能存在内存溢出
     */
    private static final ConcurrentMap<String,Integer> WAIT_AUTHORIZE_MAP = new ConcurrentHashMap<>();

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private WxMpService wxMpService;


    @Value("${wx.mp.callback}")
    private String callback;

    public static String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    @Autowired
    private UserDao userDao;

    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage) {
        // 传入二维码的code
        // 扫码用户的openID
        String fromUser = wxMpXmlMessage.getFromUser();
        Integer code = getEventKey(wxMpXmlMessage);
        if (Objects.isNull(code)) {
            return null;
        }
        User user = userService.getByOpenID(fromUser);
        // 未找到用户注册信息，进行组成
        if (Objects.isNull(user)) {
            log.info("{} :用户未注册，进入注册流程", fromUser);
            User userBuild = UserAdapter.buildUserSave(fromUser);
            userService.saveUser(userBuild);
        }
        // 用户注册成功并且授权成功
        if (Objects.nonNull(user) && Objects.nonNull(user.getAvatar())) {
            // 登录成功，通过code给channel推送消息
            webSocketService.scanLoginSuccess(code, fromUser);
            return null;
        }
        // 推送连接让用户授权
        WAIT_AUTHORIZE_MAP.put(fromUser, code);
        webSocketService.waitAuthorize(code);
        String authorURL = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback+ "/wx/portal/public/callBack"));
        return TextBuilder.build("请点击<a href=\""+authorURL+"\">登录</a>", wxMpXmlMessage);

    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openid = userInfo.getOpenid();
        User user = userDao.getByOpenID(openid);
        // 更新用户信息
        if (StrUtil.isBlank(user.getAvatar())) {
            fillUserInfo(user.getId(),userInfo);
        }
        // 通过code找到用户channel进行登录
        Integer remove = WAIT_AUTHORIZE_MAP.remove(openid);
        webSocketService.scanLoginSuccess(remove, openid);
    }

    private void fillUserInfo(Long id, WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUser(id, userInfo);
        // 用户名不重复 可增加
        userDao.updateById(user);
    }

    private Integer getEventKey (WxMpXmlMessage wxMpXmlMessage) {
        try {
            String code = wxMpXmlMessage.getEventKey();
            String qrscene = code.replace("qrscene_", "");
            return Integer.parseInt(qrscene);
        } catch (Exception e) {
            log.error("获取EventKey失败，失败原因{}", e.getMessage());
            return null;
        }
    }
}
