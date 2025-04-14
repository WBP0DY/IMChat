package com.wbp.mallchat.common.user.dao;

import com.wbp.mallchat.common.domain.YesOrNoEnum;
import com.wbp.mallchat.common.user.domain.entity.UserBackpack;
import com.wbp.mallchat.common.user.mapper.UserBackpackMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author wbp
 * @since 2025-04-06
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack> {

    public Integer getNameChangeCount(Long uid, Long itemId) {
        return lambdaQuery().eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.YES.getStatus())
                .count();
    }

    public UserBackpack getItemById(Long uid, Long itemId) {
        return lambdaQuery().eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.YES.getStatus())
                .orderByDesc(UserBackpack::getCreateTime)
                .one();
    }

    public void userItemCard(UserBackpack userBackpack) {
        lambdaUpdate().eq(UserBackpack::getId, userBackpack.getId())
                .eq(UserBackpack::getStatus, YesOrNoEnum.YES.getStatus())
                .set(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus());
    }

}
