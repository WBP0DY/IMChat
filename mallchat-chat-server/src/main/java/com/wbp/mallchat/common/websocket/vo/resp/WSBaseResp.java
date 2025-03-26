package com.wbp.mallchat.common.websocket.vo.resp;

import lombok.Data;

@Data
public class WSBaseResp<T> {
    /**
     * @see com.wbp.mallchat.common.websocket.enums.WSRespTypeEnum
     */
    private Integer type;
    private T data;
}
