package com.wbp.mallchat.common.user.dao;

import com.wbp.mallchat.common.user.domain.entity.ItemConfig;
import com.wbp.mallchat.common.user.mapper.ItemConfigMapper;
import com.wbp.mallchat.common.user.service.IItemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 功能物品配置表 服务实现类
 * </p>
 *
 * @author wbp
 * @since 2025-04-06
 */
@Service
public class ItemConfigDao extends ServiceImpl<ItemConfigMapper, ItemConfig> {

}
