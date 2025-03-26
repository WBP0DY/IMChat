package com.wbp.mallchat.common.websocket.vo.req;

import com.wbp.mallchat.common.websocket.enums.WSBaseReqEnum;
import lombok.Data;

@Data
public class WSBaseReq {
    /**
     * @see WSBaseReqEnum
     */
    private Integer type;
    private String data;
}
