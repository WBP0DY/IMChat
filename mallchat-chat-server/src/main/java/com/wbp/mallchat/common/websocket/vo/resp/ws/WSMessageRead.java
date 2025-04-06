package com.wbp.mallchat.common.websocket.vo.resp.ws;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author: wbp
 * Date: 2023-03-19
 */
@Data
@Builder
@NoArgsConstructor
public class WSMessageRead {
    /*@ApiModelProperty("消息")
    private Long msgId;
    @ApiModelProperty("阅读人数（可能为0）")
    private Integer readCount;*/
}
