package com.wbp.mallchat.common.websocket.service.adapter;

import com.wbp.mallchat.common.user.domain.entity.User;
import com.wbp.mallchat.common.websocket.enums.WSRespTypeEnum;
import com.wbp.mallchat.common.websocket.vo.resp.WSBaseResp;
import com.wbp.mallchat.common.websocket.vo.resp.ws.WSLoginSuccess;
import com.wbp.mallchat.common.websocket.vo.resp.ws.WSLoginUrl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

public class WebSocketAdapter {
    public static WSBaseResp<?> nulidResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> wsBaseResp = new WSBaseResp();
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        wsBaseResp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));
        return wsBaseResp;
    }

    public static WSBaseResp<?> buildResp(User user, String token) {
        WSBaseResp<WSLoginSuccess> wsBaseResp = new WSBaseResp();
        WSLoginSuccess wsLoginSuccess = WSLoginSuccess.builder().avatar(user.getAvatar()).name(user.getName()).token(token).uid(user.getId()).build();
        wsBaseResp.setData(wsLoginSuccess);
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        return wsBaseResp;
    }

    public static WSBaseResp<?> buildWaitAuthorize() {
        WSBaseResp<WSLoginSuccess> wsBaseResp = new WSBaseResp();
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return wsBaseResp;
    }

    public static WSBaseResp<?> buildInvalidTokenResp() {
        WSBaseResp<WSLoginSuccess> wsBaseResp = new WSBaseResp();
        wsBaseResp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return wsBaseResp;
    }
}
