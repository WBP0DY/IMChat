package com.wbp.mallchat.common.websocket.service;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

public interface WXMsgService {
    WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage);

    /**
     * 用户授权成功，填写微信ID姓名
     * @param userInfo
     */
    void authorize(WxOAuth2UserInfo userInfo);
}
