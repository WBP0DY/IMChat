package com.wbp.mallchat.common.websocket.vo.resp.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description: 群成员列表的成员信息
 * Author: wbp
 * Date: 2023-03-23
 */
@Data
@Builder
@NoArgsConstructor
public class ChatMemberResp {
    /*@ApiModelProperty("uid")
    private Long uid;
    *//**
     * @see com.abin.mallchat.common.user.domain.enums.ChatActiveStatusEnum
     *//*
    @ApiModelProperty("在线状态 1在线 2离线")
    private Integer activeStatus;

    *//**
     * 角色ID
     *//*
    private Integer roleId;

    @ApiModelProperty("最后一次上下线时间")
    private Date lastOptTime;*/
}
