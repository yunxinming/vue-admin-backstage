package com.ming.admin.service;

import com.ming.admin.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.admin.util.Ajax;

import java.util.List;

/**
 * <p>
 * 角色和菜单关联表 服务类
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {
    Ajax updateRoleMenus(List<SysRoleMenu> roleMenus);

    Ajax deleteRoleMenus(List<SysRoleMenu> roleMenus);
}
