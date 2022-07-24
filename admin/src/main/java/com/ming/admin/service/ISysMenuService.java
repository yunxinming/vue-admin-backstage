package com.ming.admin.service;

import com.ming.admin.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.admin.util.Ajax;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
public interface ISysMenuService extends IService<SysMenu> {
    List<String> findPermsByUserId(Long userid);

    List<SysMenu> findMenus();

    List<SysMenu> findAllMenu();

    Ajax saveMenus(List<SysMenu> menus);

    Ajax updateMenus(List<SysMenu> menus);

    Ajax deleteMenus(List<SysMenu> menus);
}
