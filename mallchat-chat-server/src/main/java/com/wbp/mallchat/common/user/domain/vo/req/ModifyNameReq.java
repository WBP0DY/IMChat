package com.wbp.mallchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ModifyNameReq {
    @NotNull
    @Length(max = 6, message = "用户名称不能超过6个字")
    @ApiModelProperty("用户修改后名称")
    private String name;
}
