package com.wbp.mallchat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoResp {
    @ApiModelProperty("uid")
    private Long id;
    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("用户头像")
    private String avatar;
    @ApiModelProperty("用户性别")
    private Integer sex;
    @ApiModelProperty("改名卡次数")
    private Integer modifyNameChange;
}
