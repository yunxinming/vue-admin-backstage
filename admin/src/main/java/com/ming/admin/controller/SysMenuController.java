package com.ming.admin.controller;

import com.ming.admin.entity.SysMenu;
import com.ming.admin.service.ISysMenuService;
import com.ming.admin.util.Ajax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAnyAuthority('system:menu:list', 'system:menu:query')")
    @GetMapping("/menus")
    public Ajax getMenus(){
        List<SysMenu> allMenu = menuService.findAllMenu();
        if (allMenu == null) return Ajax.error("获取失败");
        return Ajax.success(allMenu);
    }

    @PreAuthorize("hasAuthority('system:menu:add')")
    @PostMapping("/menus")
    public Ajax saveMenus(@RequestBody List<SysMenu> menus){
        return menuService.saveMenus(menus);
    }

    @PreAuthorize("hasAuthority('system:menu:edit')")
    @PutMapping("/menus")
    public Ajax updateMenus(@RequestBody List<SysMenu> menus){
        return menuService.updateMenus(menus);
    }

    @PreAuthorize("hasAuthority('system:menu:remove')")
    @DeleteMapping("/menus")
    public Ajax deleteMenus(@RequestBody List<SysMenu> menus){
        return menuService.deleteMenus(menus);
    }
}
