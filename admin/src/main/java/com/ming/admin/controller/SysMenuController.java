package com.ming.admin.controller;

import com.ming.admin.entity.SysMenu;
import com.ming.admin.service.ISysMenuService;
import com.ming.admin.util.Ajax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
@RestController
@RequestMapping("/admin")
public class SysMenuController {

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取用户拥有的菜单路由
     * @return 返回用户拥有的菜单路由表
     */
    @GetMapping("/router")
    public Ajax router(){
        List<SysMenu> menus = menuService.findMenus();
        return Ajax.success(menus);
    }
}
