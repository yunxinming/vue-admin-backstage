package com.ming.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.ming.admin.entity.SysRoleMenu;
import com.ming.admin.mapper.SysRoleMenuMapper;
import com.ming.admin.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.admin.util.Ajax;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    public Ajax updateRoleMenus(List<SysRoleMenu> roleMenus) {
        AbstractWrapper<SysRoleMenu, SFunction<SysRoleMenu, ?>, LambdaQueryWrapper<SysRoleMenu>> wrapper = lambdaQuery().getWrapper();
        List<Long> removeRoleIds = roleMenus.stream().map(SysRoleMenu::getRoleId).distinct().collect(Collectors.toList());
        removeRoleIds.forEach(rid -> {
            wrapper.eq(SysRoleMenu::getRoleId, rid);
            remove(wrapper);
        });
        boolean b1 = saveBatch(roleMenus);
        return b1 ? Ajax.success("修改成功") : Ajax.error("修改失败");
    }

    @Override
    public Ajax deleteRoleMenus(List<SysRoleMenu> roleMenus) {
        AtomicBoolean b = new AtomicBoolean(false);
        roleMenus.forEach(rm -> {
            LambdaQueryWrapper<SysRoleMenu> eq = lambdaQuery().getWrapper().eq(SysRoleMenu::getRoleId, rm.getRoleId()).eq(SysRoleMenu::getMenuId, rm.getMenuId());
            b.set(remove(eq));
        });
        return b.get() ? Ajax.success("修改成功") : Ajax.error("修改失败");
    }
}
