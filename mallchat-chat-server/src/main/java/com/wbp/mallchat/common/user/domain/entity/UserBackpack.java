package com.wbp.mallchat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户背包表
 * </p>
 *
 * @author wbp
 * @since 2025-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_backpack")
public class UserBackpack implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * uid
     */
    @TableField("uid")
    private Long uid;

    /**
     * 物品id
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 使用状态 0.待使用 1已使用
     */
    @TableField("status")
    private Integer status;

    /**
     * 幂等号
     */
    @TableField("idempotent")
    private String idempotent;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


}
